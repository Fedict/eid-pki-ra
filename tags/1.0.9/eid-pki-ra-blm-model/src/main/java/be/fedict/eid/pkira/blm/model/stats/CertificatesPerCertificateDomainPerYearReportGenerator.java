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
@Name(CertificatesPerCertificateDomainPerYearReportGenerator.NAME)
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CertificatesPerCertificateDomainPerYearReportGenerator extends ReportGeneratorHelper implements StatisticsReportGenerator {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificatesPerCertificateDomainPerYearReportGenerator";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected String getQueryString() {
		return "SELECT year(c.creationDate), c.certificateDomain.name, c.certificateDomain.dnExpression, COUNT(*)" +
				" FROM CertificateSigningContract c" +
				" WHERE c.result=:result" +
				" GROUP BY year(c.creationDate), c.certificateDomain.name, c.certificateDomain.dnExpression" +
				" ORDER BY year(c.creationDate) DESC, c.certificateDomain.name ASC";
	}

	@Override
	protected void setQueryParameters(Query query) {
		query.setParameter("result", ResultType.SUCCESS);

	}

	@Override
	protected List<Object> getValuesFromDataRow(Object[] items) {
		return Arrays.asList(getYearFromStartOfDataRow(items), items[1], items[2], items[3]);
	}

	@Override
	public List<ReportColumn> getReportColumns() {
		return Arrays.asList(
			new ReportColumn(ReportColumnType.DATE, "stats.year", "yyyy"),
			new ReportColumn(ReportColumnType.LABEL, "stats.certificateDomainName"),
			new ReportColumn(ReportColumnType.LABEL, "stats.certificateDomainExpression"),
			new ReportColumn(ReportColumnType.LONG, "stats.numberOfCertificates")
		);			
	}
	
}
