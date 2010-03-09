package be.fedict.eid.pkira.blm.model.contracthandler.services;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.jboss.seam.log.Log;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.dss.client.NotParseableXMLDocumentException;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracthandler.services.SignatureVerifierBean;


public class SignatureVerificationBeanTest {

	private static final String DOCUMENT = "DOCUMENT";
	private static final String IDENTITY = "90010110021";
	private Log log;
	private SignatureVerifierBean bean;
	private DigitalSignatureServiceClient dssClient;

	@BeforeMethod
	public void setup() {
		log = mock(Log.class);
		dssClient = mock(DigitalSignatureServiceClient.class);
		
		bean = new SignatureVerifierBean();
		bean.setLog(log);
		bean.setDigitalSignatureServiceClient(dssClient);
	}
	
	@Test
	public void testVerifySignature() throws ContractHandlerBeanException, NotParseableXMLDocumentException {
		when(dssClient.verify(eq(DOCUMENT))).thenReturn(true);
		
		String identity = bean.verifySignature(DOCUMENT);
		assertEquals(identity, IDENTITY);
	}
	
	@Test(expectedExceptions=ContractHandlerBeanException.class)
	public void testVerifySignatureInvalid() throws ContractHandlerBeanException, NotParseableXMLDocumentException {
		when(dssClient.verify(eq(DOCUMENT))).thenReturn(false);
		
		bean.verifySignature(DOCUMENT);
	}
	
	@Test(expectedExceptions=RuntimeException.class)
	public void testVerifySignatureError() throws ContractHandlerBeanException, NotParseableXMLDocumentException {
		when(dssClient.verify(eq(DOCUMENT))).thenThrow(new RuntimeException());
		
		try {
			bean.verifySignature(DOCUMENT);
			fail("Expected exception");
		} catch (RuntimeException e) {
			verify(log).error(isA(String.class), eq(e));
			throw e;
		}		
	}

}
