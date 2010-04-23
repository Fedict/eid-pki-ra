/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
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
		createReportEntry("testCA1", "testCD1", ContractType.REQUEST, "2010-01", true);
		createReportEntry("testCA1", "testCD1", ContractType.REQUEST, "2010-01", false);
		createReportEntry("testCA1", "testCD1", ContractType.REVOKE, "2010-01", true);
		createReportEntry("testCA1", "testCD2", ContractType.REQUEST, "2010-01", true);
		createReportEntry("testCA2", "testCD3", ContractType.REQUEST, "2010-01", true);
		createReportEntry("testCA2", "testCD3", ContractType.REQUEST, "2010-01", true);
		createReportEntry("testCA2", "testCD3", ContractType.REVOKE, "2010-01", false);
	}
	
	@Test
	public void testCreateReport() throws Exception {
		String report = reportManagerBean.generateReport("2010-01", true, true);
		System.out.println(report);
		
		Reader xmlReader = new InputStreamReader(ReportManagerBeanReportingTest.class.getResourceAsStream("testReport.xml"));
		Diff diff = XMLUnit.compareXML(xmlReader, report);
		
		assertTrue(diff.identical(), diff.toString());
	}

	private void createReportEntry(String certificateAuthorityName, String certificateDomainName,
			ContractType contractType, String month, boolean success) {
		ReportEntry entry = new ReportEntry();
		entry.setCertificateAuthorityName(certificateAuthorityName);
		entry.setCertificateDomainName(certificateDomainName);
		entry.setContractType(contractType);
		entry.setMonth(month);
		entry.setSuccess(success);

		persistObject(entry);
	}
}
