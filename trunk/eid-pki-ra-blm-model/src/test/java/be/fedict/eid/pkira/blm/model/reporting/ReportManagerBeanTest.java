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
package be.fedict.eid.pkira.blm.model.reporting;

import java.util.Calendar;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.ca.CertificateAuthority;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author Jan Van den Bergh
 */
public class ReportManagerBeanTest {

	private static final String CD_NAME = "certificateDomain";
	private static final String CA_NAME = "certifcateAuthority";

	@Mock
	private ReportEntryHome reportEntryHome;	
	
	private ReportManagerBean reportManager;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		reportManager = new ReportManagerBean();
		reportManager.setReportEntryHome(reportEntryHome);
	}

	@Test
	public void testAddRequestLineToReport() {
		AbstractContract contract = createCertificateSigningContract();
		ReportEntry entry = new ReportEntry();		
		when(reportEntryHome.getInstance()).thenReturn(entry);
		
		reportManager.addLineToReport(contract, true);
		
		verify(reportEntryHome).persist();
		assertEquals(entry.getCertificateAuthorityName(), CA_NAME);
		assertEquals(entry.getCertificateDomainName(), CD_NAME);
		assertEquals(entry.getRequester(), contract.getRequester());
		assertEquals(entry.getSubject(), contract.getSubject());
		assertEquals(entry.getContractType(), ReportEntry.ContractType.REQUEST);
		assertEquals(entry.getMonth(), getMonth());
	}
	
	@Test
	public void testAddRevokeLineToReport() {
		AbstractContract contract = createCertificateRevocationContract();
		ReportEntry entry = new ReportEntry();		
		when(reportEntryHome.getInstance()).thenReturn(entry);
		
		reportManager.addLineToReport(contract, true);
		
		verify(reportEntryHome).persist();
		assertEquals(entry.getCertificateAuthorityName(), CA_NAME);
		assertEquals(entry.getCertificateDomainName(), CD_NAME);
		assertEquals(entry.getRequester(), contract.getRequester());
		assertEquals(entry.getSubject(), contract.getSubject());
		assertEquals(entry.getContractType(), ReportEntry.ContractType.REVOCATION);
		assertEquals(entry.getMonth(), getMonth());
	}

	private CertificateSigningContract createCertificateSigningContract() {
		CertificateSigningContract result = new CertificateSigningContract();
		result.setCertificateDomain(createCertificateDomain());
		result.setCertificateType(CertificateType.SERVER);
		result.setContractDocument("contractDocument");
		result.setRequester("requester");
		result.setSubject("subject");
		result.setValidityPeriodMonths(15);
		return result;
	}
	
	private CertificateRevocationContract createCertificateRevocationContract() {
		CertificateRevocationContract result = new CertificateRevocationContract();
		result.setCertificateDomain(createCertificateDomain());
		result.setContractDocument("contractDocument");
		result.setRequester("requester");
		result.setSubject("subject");
		return result;
	}

	private CertificateDomain createCertificateDomain() {
		CertificateDomain result = new CertificateDomain();
		result.setCertificateAuthority(createCertificateAuthority());
		result.setName(CD_NAME);		
		return result;
	}

	private CertificateAuthority createCertificateAuthority() {
		CertificateAuthority result = new CertificateAuthority();
		result.setName(CA_NAME);
		return result;
	}
	
	private String getMonth() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		
		return year + "-" + (month<10 ? "0" : "") + month;
	}
}
