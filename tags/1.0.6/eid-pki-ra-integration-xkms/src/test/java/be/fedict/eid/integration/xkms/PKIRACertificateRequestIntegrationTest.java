package be.fedict.eid.integration.xkms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.seam.log.Log;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParserImpl;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.generated.contracts.EntityType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;
import be.fedict.eid.pkira.xkmsws.keyinfo.KeyStoreKeyProvider;
import be.fedict.eid.pkira.xkmsws.signing.XmlDocumentSigner;
import be.fedict.eid.pkira.xkmsws.util.XMLMarshallingUtil;

/**
 * Integration test that goes 'end-to-end', by submitting a contract to the BLM
 * web service.
 * 
 * @author jan
 */
public class PKIRACertificateRequestIntegrationTest {

	private static final int VALIDITY = 15;
	private static final String PHONE = "+321234567";
	private static final String NAME = "Pietje Puk";
	private static final String FUNCTION = "Certificate requester";
	private static final String LEGAL_NOTICE = "Test Legal Notice";
	private static final String CA_ID = "5001";
	private EIDPKIRAServiceClient eidPKIRAServiceClient;
	private Map<String, String> parameters;
	private EIDPKIRAContractsClient eidPKIRAContractsClient;
	private CertificateParserImpl certificateParser;

	@BeforeClass
	public void registerProvider() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@BeforeMethod
	public void readParameters() throws IOException {
		// Load the properties
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("xkms-integration.properties"));

		// Set http.proxy properties on System
		for (Object propertyKey : properties.keySet()) {
			String propertyName = propertyKey.toString();
			if (propertyName.startsWith("http.proxy")) {
				System.setProperty(propertyName, properties.getProperty(propertyName));
			}
		}

		// Create the XKMS client
		parameters = new HashMap<String, String>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			parameters.put((String) entry.getKey(), (String) entry.getValue());
		}
	}

	@BeforeMethod(dependsOnMethods = "readParameters")
	public void setupServices() {
		eidPKIRAContractsClient = new EIDPKIRAContractsClient();

		eidPKIRAServiceClient = new EIDPKIRAServiceClient();
		eidPKIRAServiceClient.setServiceUrl(parameters.get("eidpkira.service.public.url"));

		certificateParser = new CertificateParserImpl();
		certificateParser.setLog(Mockito.mock(Log.class));
	}

	@BeforeMethod(dependsOnMethods = "readParameters")
	public void initializeCAParameters() throws SQLException, ClassNotFoundException {
		Connection connection = getConnection();

		try {
			connection.setAutoCommit(false);
			executeQuery(connection, "DELETE FROM CA_PARAMETERS WHERE CA_CA_ID=?", CA_ID);
			setCAParameterValues(connection, "buc.client", "buc.code", "buc.server", "buc.persons", "signing.provider",
					"signing.keystore.type", "signing.keystore.url", "signing.keystore.entry",
					"signing.keystore.password", "signing.keystore.entry.password", "xkms.logPrefix");
			executeQuery(connection, "UPDATE CA SET XKMS_URL=? WHERE CA_ID=?", parameters.get("xkms.url"), 5001);
			connection.commit();
		} finally {
			connection.close();
		}
	}

	@BeforeMethod(dependsOnMethods = "readParameters")
	public void initializeUserCertificate() throws SQLException, ClassNotFoundException, IOException, CryptoException {
		Connection connection = getConnection();

		String certificate = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("my.crt")
				.getFile()));

		// CertificateParserImpl certificateParser = new
		// CertificateParserImpl();
		// certificateParser.setLog(Mockito.mock(Log.class));
		// String certificateSubject =
		// certificateParser.parseCertificate(certificate).getDistinguishedName();

		try {
			connection.setAutoCommit(false);
			executeQuery(
					connection,
					"UPDATE USER SET CERTIFICATE=?, CERTIFICATE_SUBJECT=? WHERE USER_ID=?",
					certificate,
					"c=BE,cn=Jan Van den Bergh,e=j.vandenbergh@aca-it.be,l=Hasselt,o=ACA-IT Solutions,st=Limburg",
					7001);
			connection.commit();
		} finally {
			connection.close();
		}
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");

		Connection connection = DriverManager.getConnection(parameters.get("eidpkira.jdbc.url"),
				parameters.get("eidpkira.jdbc.username"), parameters.get("eidpkira.jdbc.password"));
		return connection;
	}

	@Test
	public void createSignAndSubmitContract() throws XmlMarshallingException, XKMSClientException, CryptoException {
		Document requestDocument = createRequestContract();
		signRequestDocument(requestDocument);

		String requestMessage = new XMLMarshallingUtil().convertDocumentToString(requestDocument);
		System.out.println(requestMessage);

		String responseMessage = eidPKIRAServiceClient.signCertificate(requestMessage);
		System.out.println(responseMessage);

		CertificateSigningResponseType response = parseResponse(responseMessage);
		assertEquals(response.getResult(), ResultType.SUCCESS);
		assertNotNull(response.getCertificate());

		CertificateInfo info = certificateParser.parseCertificate(response.getCertificate());
		System.out.println("Certificate DN: " + info.getDistinguishedName());
		System.out.println("Certificate serial: " + info.getSerialNumber());
		System.out.println("Certificate issuer: " + info.getIssuer());
		System.out.println("Certificate:");
		System.out.println(info.getPemEncoded());
	}

	private CertificateSigningResponseType parseResponse(String responseMessage) throws XmlMarshallingException {
		return eidPKIRAContractsClient.unmarshal(responseMessage, CertificateSigningResponseType.class);
	}

	private void signRequestDocument(Document requestDocument) throws XKMSClientException {
		Map<String, String> signingKeyParameters = new HashMap<String, String>();
		signingKeyParameters.put(KeyStoreKeyProvider.PARAMETER_KEYSTORE_TYPE, "pkcs12");
		signingKeyParameters.put(KeyStoreKeyProvider.PARAMETER_KEYSTORE_URL,
				getClass().getClassLoader().getResource("my.p12").toExternalForm());
		signingKeyParameters.put(KeyStoreKeyProvider.PARAMETER_KEYSTORE_PASSWORD, "pkira");
		signingKeyParameters.put(KeyStoreKeyProvider.PARAMETER_KEYSTORE_ENTRY_PASSWORD, "pkira");

		KeyStoreKeyProvider keyProvider = new KeyStoreKeyProvider();
		keyProvider.setParameters(signingKeyParameters);

		new XmlDocumentSigner(parameters).signXKMSDocument(requestDocument, keyProvider.getCertificate(),
				keyProvider.getPrivateKey(), "CertificateSigningRequest", "CertificateSigningRequest");
	}

	private Document createRequestContract() throws XmlMarshallingException {
		CSRInfo csrInfo = Util.generateCSR();
		String distinguishedName = csrInfo.getSubject();
		String csr = csrInfo.getPemEncoded();

		EntityType operator = new EntityBuilder().setFunction(FUNCTION).setName(NAME).setPhone(PHONE).toEntityType();
		CertificateSigningRequestType request = new CertificateSigningRequestBuilder()
				.setCertificateType(CertificateTypeType.SERVER).setCsr(csr).setDescription("Sample certificate")
				.setDistinguishedName(distinguishedName).setLegalNotice(LEGAL_NOTICE).setOperator(operator)
				.setValidityPeriodMonths(VALIDITY).toRequestType();

		return eidPKIRAContractsClient.marshalToDocument(request, CertificateSigningRequestType.class);
	}

	private void setCAParameterValues(Connection connection, String... keys) throws SQLException {
		for (String key : keys) {
			if (parameters.containsKey(key)) {
				String value = parameters.get(key);

				executeQuery(connection, "INSERT INTO CA_PARAMETERS(CA_CA_ID, mapkey, element) VALUES (?,?,?)", CA_ID,
						key, value);
			}
		}
	}

	private void executeQuery(Connection connection, String query, Object... parameterValues) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(query);
		try {
			for (int i = 0; i < parameterValues.length; i++) {
				statement.setObject(i + 1, parameterValues[i]);
			}
			statement.executeUpdate();
		} finally {
			statement.close();
		}

	}
}
