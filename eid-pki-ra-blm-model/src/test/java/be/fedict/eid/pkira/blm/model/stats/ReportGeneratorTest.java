package be.fedict.eid.pkira.blm.model.stats;

import static org.testng.Assert.assertEquals;

import java.util.GregorianCalendar;
import java.util.List;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportRow;

public class ReportGeneratorTest extends DatabaseTest {

	@Test
	public void testCertificatePerYearReport() {
		CertificatesPerYearReportGenerator reportGenerator = new CertificatesPerYearReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 2);
		assertEquals(getValue(report, 0, 0), new GregorianCalendar(2012,0,1).getTime());
		assertEquals(getValue(report, 0, 1), 1L);
		assertEquals(getValue(report, 1, 0), new GregorianCalendar(2011,0,1).getTime());
		assertEquals(getValue(report, 1, 1), 1L);
	}
	
	@Test
	public void testCertificatePerMonthReport() {
		CertificatesPerMonthReportGenerator reportGenerator = new CertificatesPerMonthReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 2);
		assertEquals(getValue(report, 0, 0), new GregorianCalendar(2012,0,1).getTime());
		assertEquals(getValue(report, 0, 1), 1L);
		assertEquals(getValue(report, 1, 0), new GregorianCalendar(2011,11,1).getTime());
		assertEquals(getValue(report, 1, 1), 1L);
	}
	
	@Test
	public void testCertificatePerDayReport() {
		CertificatesPerDayReportGenerator reportGenerator = new CertificatesPerDayReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 3);
		assertEquals(report.get(0).getValues().size(), 2);
		assertEquals(getValue(report, 0, 0), new GregorianCalendar(2012,0,1).getTime());
		assertEquals(getValue(report, 0, 1), 1L);
		assertEquals(getValue(report, 1, 0), new GregorianCalendar(2011,11,31).getTime());
		assertEquals(getValue(report, 1, 1), 0L);
		assertEquals(getValue(report, 2, 0), new GregorianCalendar(2011,11,30).getTime());
		assertEquals(getValue(report, 2, 1), 1L);
	}

	private Object getValue(List<ReportRow> rows, int row, int column) {
		return rows.get(row).getValues().get(column).getValue();
	}
}
