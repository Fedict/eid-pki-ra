package be.fedict.eid.pkira.blm.model.contracthandler.services;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracthandler.services.XKMSService;
import be.fedict.eid.pkira.blm.model.contracthandler.services.XKMSServiceBean;
import be.fedict.eid.pkira.crypto.CryptoException;

public class XKMSServiceBeanTest {

	private XKMSService service;

	@BeforeTest
	public void setup(){
		service = new XKMSServiceBean(); 		
	}
	
	@Test
	public void signTest() throws CryptoException, ContractHandlerBeanException{
		String certificate = service.sign(null);
		assertNotNull(certificate);
		assertTrue(certificate.length()!=0);
	}
}
