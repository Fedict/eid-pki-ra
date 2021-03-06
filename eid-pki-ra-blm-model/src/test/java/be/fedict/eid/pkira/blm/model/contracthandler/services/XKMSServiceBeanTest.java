/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

package be.fedict.eid.pkira.blm.model.contracthandler.services;

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
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntry;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.blm.model.reporting.ReportManager;
import be.fedict.eid.pkira.crypto.certificate.CertificateInfo;
import be.fedict.eid.pkira.crypto.certificate.CertificateParser;
import be.fedict.eid.pkira.crypto.csr.CSRInfo;
import be.fedict.eid.pkira.crypto.csr.CSRParser;
import be.fedict.eid.pkira.xkmsws.XKMSClient;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

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
	
	@Mock
	private ConfigurationEntryQuery configurationEntryQuery;

	private final CertificateAuthority certificateAuthority = new CertificateAuthority();
	private final CertificateDomain certificateDomain = createCertificateDomain();

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
		service.setConfigurationEntryQuery(configurationEntryQuery);

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
		try {
			ConfigurationEntry value = new ConfigurationEntry();
			value.setKey(ConfigurationEntryKey.NOT_BEFORE_TIMESHIFT_SECOND);
			value.setValue("300");
			when(configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.NOT_BEFORE_TIMESHIFT_SECOND)).thenReturn(value );
			when(xkmsClient.createCertificate(CSR_BYTES, VALIDITY, "CODE", 300)).thenReturn(CERTIFICATE_BYTES);

			CertificateSigningContract contract = createCertificateSigningContract();
			String certificate = service.sign(contract, CSR);

			assertEquals(certificate, CERTIFICATE);
			verify(reportManager).addLineToReport(contract, true);
			verifyNoMoreInteractions(errorLogger);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test(dependsOnMethods = "signTest")
	public void signTestError() throws Exception {
		XKMSClientException exception = new XKMSClientException("Oops");
		when(xkmsClient.createCertificate(CSR_BYTES, VALIDITY, "CODE", 300)).thenThrow(exception);

		CertificateSigningContract contract = createCertificateSigningContract();
		try {

			service.sign(contract, CSR);
			fail("Expected exception.");
		} catch (ContractHandlerBeanException e) {
			// Ok
		}

		verify(reportManager).addLineToReport(contract, false);
		verify(errorLogger).logError(eq(ApplicationComponent.XKMS), anyString(), eq(exception));
	}

	@Test
	public void revocationTest() throws Exception {
		CertificateRevocationContract contract = createCertificateRevocationContract();
		service.revoke(contract, CertificateType.CODE, CERTIFICATE);

		verify(xkmsClient).revokeCertificate(SERIAL_NUMBER, "CODE");
		verify(reportManager).addLineToReport(contract, true);
		verifyNoMoreInteractions(errorLogger);
	}

	@Test
	public void revocationTestError() throws Exception {
		XKMSClientException exception = new XKMSClientException("Oops");
		doThrow(exception).when(xkmsClient).revokeCertificate(SERIAL_NUMBER, "CODE");

		CertificateRevocationContract contract = createCertificateRevocationContract();
		try {
			service.revoke(contract, CertificateType.CODE, CERTIFICATE);
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
		contract.setCertificateType(CertificateType.CODE);
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
