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
package be.fedict.eid.pkira.reports;

import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.reports.ReportType;


/**
 * @author Jan Van den Bergh
 *
 */
public class ReportsClientTest {

	private ReportsClient reportsClient = new ReportsClient();
	
	@BeforeMethod
	public void setup() {
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	@Test
	public void testMarshal() throws Exception {
		ReportType report = createReportType();
		
		String reportXml = reportsClient.marshalReport(report);
		System.out.println(reportXml);
		
		InputStream control = ReportsClientTest.class.getResourceAsStream("/exampleReport.xml");		
		Diff diff = XMLUnit.compareXML(new InputStreamReader(control), reportXml);		
		assertTrue(diff.similar(), diff.toString());
	}

	private ReportType createReportType() {
		ReportBuilder builder = new ReportBuilder();
		
		MonthlyReportBuilder monthlyReport = builder.newMonthlyReport();
		monthlyReport.setDate("2010-04");
		
		monthlyReport.newCertificateAuthorityReportBuilder()
			.setCertificateAuthorityName("Test CA")
			.setSigningRequestCounts(10, 3)
			.setRevocationRequestCounts(5, 2);
		
		monthlyReport.newCertificateDomainReportBuilder()
			.setCertificateDomainName("Test Domain")
			.setSigningRequestCounts(8, 2);
		
		monthlyReport.newCertificateDomainReportBuilder()
			.setCertificateDomainName("Test Domain 2")
			.setSigningRequestCounts(2, 1);	
		
		return builder.toXmlType();
	}
}
