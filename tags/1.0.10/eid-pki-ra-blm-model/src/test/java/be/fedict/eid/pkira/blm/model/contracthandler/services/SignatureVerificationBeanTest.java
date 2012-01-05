package be.fedict.eid.pkira.blm.model.contracthandler.services;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.dss.client.NotParseableXMLDocumentException;
import be.fedict.eid.dss.client.SignatureInfo;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.RequestType;

public class SignatureVerificationBeanTest {

	private static final String DOCUMENT = "DOCUMENT";
	private static final String SUBJECT = "C=BE,OU=Domain Control Validated,O=*.aca-it.be,CN=*.aca-it.be";
	private static final RequestType REQUEST = new CertificateSigningRequestType();

	private SignatureVerifierBean bean;
	@Mock
	private Log log;
	@Mock
	private DigitalSignatureServiceClient dssClient;
	@Mock
	WebserviceLocator webserviceLocator;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new SignatureVerifierBean();
		bean.setLog(log);
		bean.setWebserviceLocator(webserviceLocator);

		when(webserviceLocator.getDigitalSignatureServiceClient()).thenReturn(dssClient);
	}

	@Test
	public void testVerifySignature() throws ContractHandlerBeanException, NotParseableXMLDocumentException {
		when(dssClient.verifyWithSigners(isA(byte[].class), eq(SignatureVerifierBean.MIME_TYPE))).thenReturn(
				Collections.singletonList(createSignatureInfo()));

		String identity = bean.verifySignature(DOCUMENT, REQUEST);
		assertEquals(identity, SUBJECT);
	}

	@Test(expectedExceptions = ContractHandlerBeanException.class)
	public void testVerifySignatureInvalid() throws ContractHandlerBeanException, NotParseableXMLDocumentException {
		when(dssClient.verifyWithSigners(isA(byte[].class), eq(SignatureVerifierBean.MIME_TYPE))).thenReturn(null);

		bean.verifySignature(DOCUMENT, REQUEST);
	}

	@Test
	public void testVerifySignatureError() throws NotParseableXMLDocumentException {
		when(dssClient.verifyWithSigners(isA(byte[].class), eq(SignatureVerifierBean.MIME_TYPE))).thenThrow(
				new NotParseableXMLDocumentException());

		try {
			bean.verifySignature(DOCUMENT, REQUEST);
			fail("Expected exception");
		} catch (ContractHandlerBeanException e) {
			verify(log).error(isA(String.class), isA(Exception.class));
		}
	}

	private SignatureInfo createSignatureInfo() {
		try {
			InputStream input = SignatureVerificationBeanTest.class.getClassLoader().getResourceAsStream(
					"aca-it.be.crt");

			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());
			X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(input);
			return new SignatureInfo(certificate, new Date(), "");
		} catch (Exception e) {
			fail("Cannot create signature info.", e);
			return null;
		}
	}

}
