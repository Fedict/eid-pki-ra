package be.fedict.eid.pkira.blm.model.contracthandler.services;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CryptoException;

public class XKMSServiceBeanTest {

	private XKMSServiceBean service;
	
	@Mock
	private CSRParser csrParser;
	
	@Mock
	private CSRInfo csrInfo;

	@BeforeTest
	public void setup(){
		Security.addProvider(new BouncyCastleProvider());
		
		MockitoAnnotations.initMocks(this);
		
		service = new XKMSServiceBean(); 	
		service.setCsrParser(csrParser);
	}
	
	@Test
	public void signTest() throws CryptoException, ContractHandlerBeanException{
		when(csrParser.parseCSR(anyString())).thenReturn(csrInfo);
		when(csrInfo.getSubject()).thenReturn("c=be,o=test");
		
		String certificate = service.sign("");
		System.err.println(certificate);
		assertNotNull(certificate);
		assertTrue(certificate.length()!=0);
	}
	
	
}
