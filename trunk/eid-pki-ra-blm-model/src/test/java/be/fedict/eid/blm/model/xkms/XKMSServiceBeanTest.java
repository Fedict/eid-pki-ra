package be.fedict.eid.blm.model.xkms;

import java.security.cert.X509Certificate;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



public class XKMSServiceBeanTest {

	private XKMSService service;

	@BeforeTest
	public void setup(){
		service = new XKMSServiceBean(); 
	}
	
	@Test
	public void signTest(){
		X509Certificate sign = service.sign();
		Assert.assertNotNull(sign);
	}
}
