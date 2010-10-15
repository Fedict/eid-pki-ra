package be.fedict.eid.integration.xkms;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParserImpl;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParserImpl;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.xkmsws.XKMSClient;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;

public class XKMSIntegrationTest {

	private static final String PARAMETER_XKMS_URL = "xkms.url";
	private static final String PREFIX = "C=be,O=fedict,OU=test,CN=test-";
	private static final Provider PROVIDER = new BouncyCastleProvider();

	@Mock
	private Log log;

	private CSRParserImpl csrParser;
	private XKMSClient xkmsClient;
	private CertificateParserImpl certificateParser;
	private static BigInteger serialNumber;

	@BeforeClass
	public void registerProvider() {
		Security.addProvider(PROVIDER);
	}

	@BeforeMethod
	public void createMocksAndXkmsClient() throws IOException {
		// Create the mocks
		MockitoAnnotations.initMocks(this);

		csrParser = new CSRParserImpl();
		csrParser.setLog(log);

		certificateParser = new CertificateParserImpl();
		certificateParser.setLog(log);

		// Load the properties and set them on System
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("xkms-integration.properties"));
		for (Map.Entry<Object, Object> property : properties.entrySet()) {
			System.setProperty(property.getKey().toString(), property.getValue().toString());
		}

		// Create the XKMS client
		Map<String, String> parameters = new HashMap<String, String>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			parameters.put((String) entry.getKey(), (String) entry.getValue());
		}
		xkmsClient = new XKMSClient(properties.getProperty(PARAMETER_XKMS_URL), parameters);
	}

	@Test
	public void testXKMSCreateCertificate() throws XKMSClientException, CryptoException {
		// CSR preparation
		byte[] csr = generateCSR();
		assertNotNull(csr);
		assertTrue(csr.length > 0);

		CSRInfo csrInfo = csrParser.parseCSR(csr);
		assertNotNull(csrInfo);
		assertTrue(csrInfo.getSubject().startsWith(PREFIX));

		// Generate certificate
		byte[] certificate = xkmsClient.createCertificate(csr, 15, "client");
		assertNotNull(certificate);

		CertificateInfo certificateInfo = certificateParser.parseCertificate(certificate);
		// assertEquals(certificateInfo.getDistinguishedName(),
		// csrInfo.getSubject());
		serialNumber = certificateInfo.getSerialNumber();
	}

	@Test(dependsOnMethods = "testXKMSCreateCertificate")
	public void testXKMSRevokeCertificateRequest() throws XKMSClientException, CryptoException {
		// Revoke the certificate
		xkmsClient.revokeCertificate(serialNumber, "client");
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
}
