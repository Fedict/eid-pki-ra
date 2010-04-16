package be.fedict.eid.pkira.blm.model.contracthandler.services;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.dss.client.NotParseableXMLDocumentException;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;


public class SignatureVerificationBeanTest {

	private static final String DOCUMENT = "DOCUMENT";
	private static final String IDENTITY = "90010110021";	
	
	private SignatureVerifierBean bean;	
	@Mock
	private Log log;
	@Mock
	private DigitalSignatureServiceClient dssClient;
	@Mock WebserviceLocator webserviceLocator;

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
		when(dssClient.verifyWithSignerIdentity(eq(DOCUMENT))).thenReturn(IDENTITY);
		
		String identity = bean.verifySignature(DOCUMENT);
		assertEquals(identity, IDENTITY);
	}
	
	@Test(expectedExceptions=ContractHandlerBeanException.class)
	public void testVerifySignatureInvalid() throws ContractHandlerBeanException, NotParseableXMLDocumentException {
		when(dssClient.verifyWithSignerIdentity(eq(DOCUMENT))).thenReturn(null);
		
		bean.verifySignature(DOCUMENT);
	}
	
	@Test
	public void testVerifySignatureError() {
		when(dssClient.verifyWithSignerIdentity(eq(DOCUMENT))).thenThrow(new RuntimeException());
		
		try {
			bean.verifySignature(DOCUMENT);
			fail("Expected exception");
		} catch (ContractHandlerBeanException e) {
			verify(log).error(isA(String.class), isA(Exception.class));
		}		
	}

}
