import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.seam.log.Log;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParserImpl;
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
public class CertificateRequestDemo {

	private static final int VALIDITY = 15;
	private static final String LEGAL_NOTICE = "Test Legal Notice";

	private final EIDPKIRAServiceClient eidPKIRAServiceClient;
	private final EIDPKIRAContractsClient eidPKIRAContractsClient;
	private Map<String, String> parameters;
	private final CertificateParserImpl certificateParser;
	private final CSRParserImpl csrParser;
	private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws Exception {
		CertificateRequestDemo demo = new CertificateRequestDemo();

		demo.createSignAndSubmitContract();
	}

	private CertificateRequestDemo() {
		Security.addProvider(new BouncyCastleProvider());

		certificateParser = new CertificateParserImpl();
		certificateParser.setLog(Mockito.mock(Log.class));

		csrParser = new CSRParserImpl();
		csrParser.setLog(Mockito.mock(Log.class));

		eidPKIRAContractsClient = new EIDPKIRAContractsClient();

		eidPKIRAServiceClient = new EIDPKIRAServiceClient();
		eidPKIRAServiceClient.setServiceUrl("http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAService");
	}

	private void createSignAndSubmitContract() throws XmlMarshallingException, XKMSClientException, CryptoException,
			IOException {
		Document requestDocument = createRequestContract();
		signRequestDocument(requestDocument);

		String requestMessage = new XMLMarshallingUtil().convertDocumentToString(requestDocument);

		System.out.println();
		System.out.println("REQUEST");
		System.out.println(requestMessage);
		System.out.println();

		String responseMessage = eidPKIRAServiceClient.signCertificate(requestMessage);
		System.out.println("RESPONSE");
		System.out.println(responseMessage);
		System.out.println();

		CertificateSigningResponseType response = parseResponse(responseMessage);
		if (response.getResult() != ResultType.SUCCESS) {
			System.out.println("Error creating certificate.");
			System.out.println("Result code: " + response.getResult());
			System.out.println("Result message: " + response.getResultMessage());
		} else {
			CertificateInfo info = certificateParser.parseCertificate(response.getCertificate());
			System.out.println("Certificate DN: " + info.getDistinguishedName());
			System.out.println("Certificate serial: " + info.getSerialNumber());
			System.out.println("Certificate issuer: " + info.getIssuer());
			System.out.println("Certificate:");
			System.out.println(info.getPemEncoded());
		}
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

	private Document createRequestContract() throws XmlMarshallingException, IOException, CryptoException {
		String csrFileName = readParameter("CSR file name (PEM format)");
		String name = readParameter("Operator name");
		String function = readParameter("Operator function");
		String phone = readParameter("Operator phone");
		String description = readParameter("Description");

		String csrData = FileUtils.readFileToString(new File(csrFileName));
		CSRInfo csrInfo = csrParser.parseCSR(csrData);
		String distinguishedName = csrInfo.getSubject();
		String csr = csrInfo.getPemEncoded();

		EntityType operator = new EntityBuilder().setFunction(function).setName(name).setPhone(phone).toEntityType();
		CertificateSigningRequestType request = new CertificateSigningRequestBuilder()
				.setCertificateType(CertificateTypeType.SERVER).setCsr(csr).setDescription(description)
				.setDistinguishedName(distinguishedName).setLegalNotice(LEGAL_NOTICE).setOperator(operator)
				.setValidityPeriodMonths(VALIDITY).toRequestType();

		return eidPKIRAContractsClient.marshalToDocument(request, CertificateSigningRequestType.class);
	}

	private String readParameter(String msg) throws IOException {
		System.out.print(msg + ": ");
		System.out.flush();

		return input.readLine();
	}
}
