package be.fedict.eid.pkira.blm.model.stats;

import static be.fedict.eid.pkira.blm.model.stats.ReportGeneratorHelper.createDate;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportRow;

public class ReportGeneratorTest extends DatabaseTest {

	@Test
	public void testCertificatePerYearReport() {
		ReportGeneratorHelper reportGenerator = new CertificatesPerYearReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 2);
		assertEquals(getValue(report, 0, 0), createDate(1, 1, 2012));
		assertEquals(getValue(report, 0, 1), 1L);
		assertEquals(getValue(report, 1, 0), createDate(1, 1, 2011));
		assertEquals(getValue(report, 1, 1), 1L);
	}
	
	@Test
	public void testCertificatePerMonthReport() {
		ReportGeneratorHelper reportGenerator = new CertificatesPerMonthReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 2);
		assertEquals(getValue(report, 0, 0), createDate(1, 1, 2012));
		assertEquals(getValue(report, 0, 1), 1L);
		assertEquals(getValue(report, 1, 0), createDate(1, 12, 2011));
		assertEquals(getValue(report, 1, 1), 1L);
	}
	
	@Test
	public void testCertificatePerDayReport() {
		ReportGeneratorHelper reportGenerator = new CertificatesPerDayReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 2);
		assertEquals(getValue(report, 0, 0), createDate(1, 1, 2012));
		assertEquals(getValue(report, 0, 1), 1L);
		assertEquals(getValue(report, 1, 0), createDate(30, 12, 2011));
		assertEquals(getValue(report, 1, 1), 1L);
	}
	
	@Test
	public void testCertificatePerCertificateDomainAndYearReport() {
		ReportGeneratorHelper reportGenerator = new CertificatesPerCertificateDomainPerYearReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 4);
		assertEquals(getValue(report, 0, 0), createDate(1, 1, 2012));
		assertEquals(getValue(report, 0, 1), "eHealth Server Certificates");
		assertEquals(getValue(report, 0, 2), "c=be,ou=eHealth,cn=*");
		assertEquals(getValue(report, 0, 3), 1L);
		assertEquals(getValue(report, 1, 0), createDate(1, 1, 2011));
		assertEquals(getValue(report, 1, 1), "eHealth Client Certificates");
		assertEquals(getValue(report, 1, 2), "c=be,ou=eHealth,uid=*");
		assertEquals(getValue(report, 1, 3), 1L);
	}
	
	@Test
	public void testCertificatePerCertificateDomainAndMonthReport() {
		ReportGeneratorHelper reportGenerator = new CertificatesPerCertificateDomainPerMonthReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 4);
		assertEquals(getValue(report, 0, 0), createDate(1, 1, 2012));
		assertEquals(getValue(report, 0, 1), "eHealth Server Certificates");
		assertEquals(getValue(report, 0, 2), "c=be,ou=eHealth,cn=*");
		assertEquals(getValue(report, 0, 3), 1L);
		assertEquals(getValue(report, 1, 0), createDate(1, 12, 2011));
		assertEquals(getValue(report, 1, 1), "eHealth Client Certificates");
		assertEquals(getValue(report, 1, 2), "c=be,ou=eHealth,uid=*");
		assertEquals(getValue(report, 1, 3), 1L);
	}
	
	@Test
	public void testCertificatePerCertificateDomainAndDayReport() {
		ReportGeneratorHelper reportGenerator = new CertificatesPerCertificateDomainPerDayReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 4);
		assertEquals(getValue(report, 0, 0), createDate(1, 1, 2012));
		assertEquals(getValue(report, 0, 1), "eHealth Server Certificates");
		assertEquals(getValue(report, 0, 2), "c=be,ou=eHealth,cn=*");
		assertEquals(getValue(report, 0, 3), 1L);
		assertEquals(getValue(report, 1, 0), createDate(30, 12, 2011));
		assertEquals(getValue(report, 1, 1), "eHealth Client Certificates");
		assertEquals(getValue(report, 1, 2), "c=be,ou=eHealth,uid=*");
		assertEquals(getValue(report, 1, 3), 1L);
	}

	private Object getValue(List<ReportRow> rows, int row, int column) {
		return rows.get(row).getValues().get(column).getValue();
	}
}
