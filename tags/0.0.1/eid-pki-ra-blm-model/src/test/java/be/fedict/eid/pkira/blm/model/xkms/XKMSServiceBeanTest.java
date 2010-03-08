package be.fedict.eid.pkira.blm.model.xkms;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.xkms.XKMSService;
import be.fedict.eid.pkira.blm.model.xkms.XKMSServiceBean;
import be.fedict.eid.pkira.crypto.CryptoException;

public class XKMSServiceBeanTest {

	private XKMSService service;

	@BeforeTest
	public void setup(){
		service = new XKMSServiceBean(); 		
	}
	
	@Test
	public void signTest() throws CryptoException{
		String certificate = service.sign(null);
		assertNotNull(certificate);
		assertTrue(certificate.length()!=0);
	}
}
