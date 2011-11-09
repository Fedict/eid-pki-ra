package be.fedict.eid.pkira.blm.stats;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.stats.StatisticsReportGenerator.ReportRow;

public class CertificatesPerYearReportGeneratorTest extends DatabaseTest {

	@Test
	public void getReport() {
		CertificatesPerYearReportGenerator reportGenerator = new CertificatesPerYearReportGenerator();
		reportGenerator.setEntityManager(getEntityManager());
		
		List<ReportRow> report = reportGenerator.getReport();
		
		assertEquals(report.size(), 2);
		assertEquals(report.get(0).getValues().size(), 2);
		assertEquals(getValue(report, 0, 0), 2012L);
		assertEquals(getValue(report, 0, 1), 1L);
		assertEquals(getValue(report, 1, 0), 2011L);
		assertEquals(getValue(report, 1, 1), 1L);
	}

	private Object getValue(List<ReportRow> rows, int row, int column) {
		return rows.get(row).getValues().get(column).getValue();
	}
}
