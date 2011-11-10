package be.fedict.eid.pkira.blm.model.stats;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Report of the number of certificates per day.
 * 
 * @author jan
 */
@Name(CertificatesPerYearReportGenerator.NAME)
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CertificatesPerYearReportGenerator extends DateToLongReportGenerator implements StatisticsReportGenerator {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificatesPerYearReportGenerator";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected String getQueryString() {
		return "SELECT year(creationDate), COUNT(*) FROM CertificateSigningContract WHERE result=:result GROUP BY year(creationDate)";
	}

	@Override
	protected void setQueryParameters(Query query) {
		query.setParameter("result", ResultType.SUCCESS);

	}

	@Override
	protected Date getKeyFromDataRow(Object[] theItem) {
		int year = (Integer) theItem[0];
		return new GregorianCalendar(year, 0, 1).getTime();
	}

	@Override
	protected Long getValueFromDataRow(Object[] theItem) {
		return (Long) theItem[1];
	}

	@Override
	protected ReportColumn getKeyColumn() {
		return new ReportColumn(ReportColumnType.DATE, "stats.year");
	}

	@Override
	protected ReportColumn getValueColumn() {
		return new ReportColumn(ReportColumnType.LONG, "stats.numberOfCertificates");
	}

	@Override
	protected void decrementIteratorDate(Calendar current) {
		current.add(Calendar.YEAR, -1);
	}
}
