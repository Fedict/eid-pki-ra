package be.fedict.eid.pkira.blm.model.contracthandler.services;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.math.BigInteger;
import java.security.Security;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.errorhandling.ApplicationComponent;
import be.fedict.eid.pkira.blm.errorhandling.ErrorLogger;
import be.fedict.eid.pkira.blm.model.ca.CertificateAuthority;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.blm.model.reporting.ReportManager;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.xkmsws.XKMSClient;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;

public class XKMSServiceBeanTest {

	private static final Integer VALIDITY = 15;
	private static final String CSR = "CSR";
	private static final byte[] CSR_BYTES = new byte[1];
	private static final byte[] CERTIFICATE_BYTES = new byte[2];
	private static final String CERTIFICATE = "CERTIFICATE";
	private static final Date START_DATE = new Date();
	private static final Date END_DATE = new Date(System.currentTimeMillis() + 10000);
	private static final BigInteger SERIAL_NUMBER = BigInteger.TEN;

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

	@Mock
	private ErrorLogger errorLogger;
	
	@Mock
	private ReportManager reportManager;

	private CertificateAuthority certificateAuthority = new CertificateAuthority();
	private CertificateDomain certificateDomain = createCertificateDomain();

	@BeforeTest
	public void setup() throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		MockitoAnnotations.initMocks(this);

		service = new XKMSServiceBean();
		service.setCsrParser(csrParser);
		service.setWebserviceLocator(webserviceLocator);
		service.setCertificateParser(certificateParser);
		service.setErrorLogger(errorLogger);
		service.setReportManager(reportManager);

		when(csrParser.parseCSR(CSR)).thenReturn(csrInfo);
		when(csrInfo.getDerEncoded()).thenReturn(CSR_BYTES);
		when(webserviceLocator.getXKMSClient(certificateAuthority)).thenReturn(xkmsClient);
		when(certificateParser.parseCertificate(CERTIFICATE_BYTES)).thenReturn(certificateInfo);
		when(certificateParser.parseCertificate(CERTIFICATE)).thenReturn(certificateInfo);
		when(certificateInfo.getPemEncoded()).thenReturn(CERTIFICATE);
		when(certificateInfo.getSerialNumber()).thenReturn(SERIAL_NUMBER);		
	}

	@Test
	public void signTest() throws Exception {
		when(xkmsClient.createCertificate(CSR_BYTES, VALIDITY)).thenReturn(CERTIFICATE_BYTES);
		
		CertificateSigningContract contract = createCertificateSigningContract();			
		String certificate = service.sign(contract);
		
		assertEquals(certificate, CERTIFICATE);
		verify(reportManager).addLineToReport(contract, true);
		verifyNoMoreInteractions(errorLogger);
	}

	@Test
	public void signTestError() throws Exception {
		XKMSClientException exception = new XKMSClientException("Oops");
		when(xkmsClient.createCertificate(CSR_BYTES, VALIDITY)).thenThrow(exception);

		CertificateSigningContract contract = createCertificateSigningContract();
		try {
			
			service.sign(contract);
			fail("Expected exception.");
		} catch (ContractHandlerBeanException e) {
			// Ok
		}
		
		verify(reportManager).addLineToReport(contract, false);
		verify(errorLogger).logError(eq(ApplicationComponent.XKMS), anyString(), eq(exception));
	}
	
	@Test
	public void revocationTest() throws Exception  {
		CertificateRevocationContract contract = createCertificateRevocationContract();
		service.revoke(contract);
		
		verify(xkmsClient).revokeCertificate(SERIAL_NUMBER);
		verify(reportManager).addLineToReport(contract, true);
		verifyNoMoreInteractions(errorLogger);
	}
	
	@Test
	public void revocationTestError() throws Exception {
		XKMSClientException exception = new XKMSClientException("Oops");
		doThrow(exception).when(xkmsClient).revokeCertificate(SERIAL_NUMBER);

		CertificateRevocationContract contract = createCertificateRevocationContract();
		try {			
			service.revoke(contract);
			fail("Expected exception.");
		} catch (ContractHandlerBeanException e) {
			// Ok
		}
		
		verify(reportManager).addLineToReport(contract, false);
		verify(errorLogger).logError(eq(ApplicationComponent.XKMS), anyString(), eq(exception));
	}

	private CertificateSigningContract createCertificateSigningContract() {
		CertificateSigningContract contract = new CertificateSigningContract();
		contract.setContractDocument(CSR);
		contract.setCertificateDomain(certificateDomain);
		contract.setValidityPeriodMonths(VALIDITY);
		return contract;
	}

	private CertificateRevocationContract createCertificateRevocationContract() {
		CertificateRevocationContract contract = new CertificateRevocationContract();
		contract.setContractDocument(CERTIFICATE);
		contract.setCertificateDomain(certificateDomain);
		contract.setStartDate(START_DATE);
		contract.setEndDate(END_DATE);
		return contract;
	}

	private CertificateDomain createCertificateDomain() {
		CertificateDomain certificateDomain = new CertificateDomain();
		certificateDomain.setCertificateAuthority(certificateAuthority);
		return certificateDomain;
	}
}
