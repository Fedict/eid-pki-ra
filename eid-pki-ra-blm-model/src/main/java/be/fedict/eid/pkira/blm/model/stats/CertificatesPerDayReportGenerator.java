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
@Name(CertificatesPerDayReportGenerator.NAME)
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CertificatesPerDayReportGenerator extends DateToLongReportGenerator implements StatisticsReportGenerator {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificatesPerDayReportGenerator";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected ReportColumn getValueColumn() {
		return new ReportColumn(ReportColumnType.LONG, "stats.numberOfCertificates");
	}

	@Override
	protected ReportColumn getKeyColumn() {
		return new ReportColumn(ReportColumnType.DATE, "stats.month");
	}

	@Override
	protected Long getValueFromDataRow(Object[] theItem) {
		return (Long) theItem[3];
	}

	@Override
	protected Date getKeyFromDataRow(Object[] theItem) {
		int day = (Integer) theItem[0];
		int month = (Integer) theItem[1];
		int year = (Integer) theItem[2];
		return new GregorianCalendar(year, month - 1, day).getTime();
	}

	@Override
	protected void setQueryParameters(Query query) {
		query.setParameter("result", ResultType.SUCCESS);
	}

	@Override
	protected String getQueryString() {
		return "SELECT day(creationDate),month(creationDate),year(creationDate), COUNT(*) FROM CertificateSigningContract WHERE result=:result GROUP BY day(creationDate),month(creationDate),year(creationDate)";
	}

	@Override
	protected void decrementIteratorDate(Calendar current) {
		current.add(Calendar.DATE, -1);
	}
}
