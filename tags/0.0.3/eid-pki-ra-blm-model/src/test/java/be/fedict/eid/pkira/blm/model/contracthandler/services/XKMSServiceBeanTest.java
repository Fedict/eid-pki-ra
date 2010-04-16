package be.fedict.eid.pkira.blm.model.contracthandler.services;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.ca.CertificateAuthority;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.xkmsws.XKMSClient;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;

public class XKMSServiceBeanTest {


	private static final Integer VALIDITY = 15;
	private static final String CSR = "CSR";
	private static final byte[] CSR_BYTES = new byte[1];
	private static final byte[] CERTIFICATE_BYTES = new byte[2];
	private static final String CERTIFICATE = "CERTIFICATE";

	private XKMSServiceBean service;
	
	@Mock
	private CSRParser csrParser;
	
	@Mock
	private CSRInfo csrInfo;
	
	@Mock
	private WebserviceLocator webserviceLocator;
	
	@Mock
	private XKMSClient xkmsClient;
	
	@Mock
	private CertificateParser certificateParser;
	
	@Mock
	private CertificateInfo certificateInfo;

	@BeforeTest
	public void setup(){
		Security.addProvider(new BouncyCastleProvider());
		
		MockitoAnnotations.initMocks(this);
		
		service = new XKMSServiceBean(); 	
		service.setCsrParser(csrParser);
		service.setWebserviceLocator(webserviceLocator);
		service.setCertificateParser(certificateParser);
	}
	
	@Test
	public void signTest() throws CryptoException, ContractHandlerBeanException, XKMSClientException{
		CertificateAuthority certificateAuthority = new CertificateAuthority();		
		CertificateDomain certificateDomain = new CertificateDomain();
		certificateDomain.setCertificateAuthority(certificateAuthority);
		CertificateSigningContract contract = new CertificateSigningContract();
		contract.setContractDocument(CSR);		
		contract.setCertificateDomain(certificateDomain);
		contract.setValidityPeriodMonths(VALIDITY);
		
		when(csrParser.parseCSR(CSR)).thenReturn(csrInfo);
		when(csrInfo.getDerEncoded()).thenReturn(CSR_BYTES);
		when(webserviceLocator.getXKMSClient(certificateAuthority)).thenReturn(xkmsClient);
		when(xkmsClient.createCertificate(CSR_BYTES, VALIDITY)).thenReturn(CERTIFICATE_BYTES);
		when(certificateParser.parseCertificate(CERTIFICATE_BYTES)).thenReturn(certificateInfo);
		when(certificateInfo.getPemEncoded()).thenReturn(CERTIFICATE);
		
		String certificate = service.sign(contract);
		assertEquals(certificate, CERTIFICATE);
	}
	
	
}
