package be.fedict.eid.integration.xkms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParserImpl;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParserImpl;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.xkmsws.XKMSClient;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;
import be.fedict.eid.pkira.xkmsws.XKMSSignatureSOAPHandler;

public class XKMSIntegrationTest {

	private static class KeyValueKeySelector extends KeySelector {

		@Override
		public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method,
				XMLCryptoContext context) throws KeySelectorException {
			return new KeySelectorResult() {

				@Override
				public Key getKey() {
					try {
						KeyStore keystore = KeyStore.getInstance("pkcs12");
						keystore.load(new FileInputStream("src/test/resources/gov-test.p12"), "G0vT3st".toCharArray());

						String alias = keystore.aliases().nextElement();
						return keystore.getCertificate(alias).getPublicKey();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
		}
	}

	private static final String PARAMETER_XKMS_URL = "xkms.url";
	private static final String PREFIX = "C=be,O=fedict,OU=test,CN=test-";
	private static final Provider PROVIDER = new BouncyCastleProvider();

	private static BigInteger serialNumber;

	private CertificateParserImpl certificateParser;
	private CSRParserImpl csrParser;
	@Mock
	private Log log;
	private XKMSClient xkmsClient;
	private Map<String, String> xkmsClientParameters;

	@BeforeClass
	public void registerProvider() {
		Security.addProvider(PROVIDER);
	}

	@BeforeMethod
	public void setup() throws IOException {
		// Create the mocks
		MockitoAnnotations.initMocks(this);

		// Create parsers
		csrParser = new CSRParserImpl();
		csrParser.setLog(log);

		certificateParser = new CertificateParserImpl();
		certificateParser.setLog(log);

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
		xkmsClientParameters = new HashMap<String, String>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			xkmsClientParameters.put((String) entry.getKey(), (String) entry.getValue());
		}
		xkmsClient = new XKMSClient(properties.getProperty(PARAMETER_XKMS_URL), xkmsClientParameters);
	}

	@Test
	public void testXKMSCreateCertificate() throws XKMSClientException, CryptoException {
		// Generate a CSR and validate it
		byte[] csr = generateCSR();

		assertNotNull(csr);
		assertTrue(csr.length > 0);

		CSRInfo csrInfo = csrParser.parseCSR(csr);
		assertNotNull(csrInfo);
		assertTrue(csrInfo.getSubject().startsWith(PREFIX));

		// Generate the certificate
		xkmsClientParameters.put(XKMSSignatureSOAPHandler.PARAMETER_LOG_PREFIX, "target/certificate-request");
		byte[] certificate = xkmsClient.createCertificate(csr, 15, "client");
		assertNotNull(certificate);

		CertificateInfo certificateInfo = certificateParser.parseCertificate(certificate);
		assertEquals(certificateInfo.getDistinguishedName(), csrInfo.getSubject());
		serialNumber = certificateInfo.getSerialNumber();
	}

	@Test(dependsOnMethods = "testXKMSCreateCertificate", alwaysRun = true)
	public void validateXKMSCreateCertificateSignature() throws Exception {
		validateSignature("target/certificate-request-signed.xml");
	}

	@Test(dependsOnMethods = "testXKMSCreateCertificate")
	public void testXKMSRevokeCertificateRequest() throws XKMSClientException, CryptoException {
		// Revoke the certificate
		xkmsClientParameters.put(XKMSSignatureSOAPHandler.PARAMETER_LOG_PREFIX, "target/revocation-request");
		xkmsClient.revokeCertificate(serialNumber, "client");
	}

	@Test(dependsOnMethods = "testXKMSCreateCertificate", alwaysRun = true)
	public void validateXKMSRevokeCertificateSignature() throws Exception {
		validateSignature("target/revocation-request-signed.xml");
	}

	private byte[] generateCSR() {
		try {
			// CSR Subject
			String subject = PREFIX + System.currentTimeMillis();
			X509Name xname = new X509Name(subject);

			// CSR Key Pair
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", PROVIDER);
			KeyPair generateKeyPair = generator.generateKeyPair();
			PrivateKey priv = generateKeyPair.getPrivate();
			PublicKey pub = generateKeyPair.getPublic();

			// CSR
			PKCS10CertificationRequest csr = new PKCS10CertificationRequest("MD5WithRSA", xname, pub, null, priv);
			return csr.getEncoded();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void validateSignature(String xmlFileName) throws Exception {
		System.err.println("Validating " + xmlFileName + "...");

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.parse(new File(xmlFileName));

		NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		if (nl.getLength() == 0) {
			throw new Exception("Cannot find Signature element");
		}

		DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(0));
		XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");
		XMLSignature signature = factory.unmarshalXMLSignature(valContext);

		boolean sv = signature.getSignatureValue().validate(valContext);
		System.err.println("Signature validation status: " + sv);

		@SuppressWarnings("rawtypes")
		Iterator i = signature.getSignedInfo().getReferences().iterator();
		for (int j = 0; i.hasNext(); j++) {
			Reference reference = (Reference) i.next();
			boolean refValid = reference.validate(valContext);
			System.err.println("Reference " + j + ", " + reference.getURI() + ": validity status: " + refValid);
		}

		assertTrue(signature.validate(valContext));
	}
}
