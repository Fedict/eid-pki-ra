/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
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

import static org.testng.Assert.assertTrue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.reporting.ReportEntry.ContractType;
import be.fedict.eid.pkira.reports.ReportsClient;

/**
 * @author Jan Van den Bergh
 */
public class ReportManagerBeanReportingTest extends DatabaseTest {

	private static final String MONTH = "2010-01";
	private ReportManagerBean reportManagerBean;
	
	@BeforeMethod
	public void setup() {
		reportManagerBean = new ReportManagerBean();
		reportManagerBean.setEntityManager(getEntityManager());
		reportManagerBean.setReportsClient(new ReportsClient());
		
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	@BeforeClass
	public void insertTestData() {
		createReportEntry("testCA1", "testCD1", ContractType.REQUEST, MONTH, true, 1);
		createReportEntry("testCA1", "testCD1", ContractType.REQUEST, MONTH, false, 2);
		createReportEntry("testCA1", "testCD1", ContractType.REVOCATION, MONTH, true, 3);
		createReportEntry("testCA1", "testCD2", ContractType.REQUEST, MONTH, true, 4);
		createReportEntry("testCA2", "testCD3", ContractType.REQUEST, MONTH, true, 5);
		createReportEntry("testCA2", "testCD3", ContractType.REQUEST, MONTH, true, 6);
		createReportEntry("testCA2", "testCD3", ContractType.REVOCATION, MONTH, false, 7);
	}
	
	@Test
	public void testCreateReport() throws Exception {
		String report = reportManagerBean.generateReport(MONTH, MONTH, true, true);
		System.out.println(report);
		
		Reader xmlReader = new InputStreamReader(ReportManagerBeanReportingTest.class.getResourceAsStream("testReport.xml"));
		Diff diff = XMLUnit.compareXML(xmlReader, report);
		
		assertTrue(diff.identical(), diff.toString());
	}

	private void createReportEntry(String certificateAuthorityName, String certificateDomainName,
			ContractType contractType, String month, boolean success, int number) {
		ReportEntry entry = new ReportEntry();
		entry.setCertificateAuthorityName(certificateAuthorityName);
		entry.setCertificateDomainName(certificateDomainName);
		entry.setContractType(contractType);
		entry.setSubject("Subject " + number);
		entry.setRequester("Requester " + number);
		entry.setMonth(month);
		entry.setSuccess(success);
		entry.setLogTime(new Date(1272307284000L + number*1000*55));

		persistObject(entry);
	}
}
