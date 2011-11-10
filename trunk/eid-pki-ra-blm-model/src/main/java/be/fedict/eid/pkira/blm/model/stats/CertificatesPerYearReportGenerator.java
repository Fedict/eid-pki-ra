package be.fedict.eid.pkira.blm.model.stats;

import java.util.Arrays;
import java.util.List;

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
public class CertificatesPerYearReportGenerator extends ReportGeneratorHelper implements StatisticsReportGenerator {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificatesPerYearReportGenerator";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected String getQueryString() {
		return "SELECT year(creationDate), COUNT(*)" +
				" FROM CertificateSigningContract" +
				" WHERE result=:result" +
				" GROUP BY year(creationDate)" +
				" ORDER BY year(creationDate) DESC";
	}

	@Override
	protected void setQueryParameters(Query query) {
		query.setParameter("result", ResultType.SUCCESS);
	}

	@Override
	protected List<Object> getValuesFromDataRow(Object[] items) {
		return Arrays.asList(getYearFromStartOfDataRow(items), items[1]);
	}

	@Override
	public List<ReportColumn> getReportColumns() {
		return Arrays.asList(new ReportColumn(ReportColumnType.DATE, "stats.year", "yyyy"), new ReportColumn(
				ReportColumnType.LONG, "stats.numberOfCertificates"));
	}
}
