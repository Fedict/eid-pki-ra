package be.fedict.eid.pkira.blm.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class CertificatesPerYearReportGenerator implements StatisticsReportGenerator {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificatesPerYearReportGenerator";

	@PersistenceContext
	private EntityManager entityManager;

	private static final ReportColumn YEAR_COLUMN = new ReportColumn(ReportColumnType.DATE, "stats.year");
	private static final ReportColumn NUMBER_COLUMN = new ReportColumn(ReportColumnType.LONG, "stats.numberOfCertificates");
	private static final List<ReportColumn> COLUMNS = Arrays.asList(YEAR_COLUMN, NUMBER_COLUMN);

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<ReportColumn> getReportColumns() {
		return COLUMNS;
	}

	@Override
	public List<ReportRow> getReport() {
		// Execute the query
		String queryStr = "SELECT year(creationDate), COUNT(*) FROM CertificateSigningContract WHERE result=:result GROUP BY year(creationDate)";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("result", ResultType.SUCCESS);
		List<?> queryResults = query.getResultList();
		
		// Convert the result to a map, keeping track of start and end dates
		Map<Integer, Long> dataMap = new HashMap<Integer, Long>();
		int minYear = Integer.MAX_VALUE;
		int maxYear = 0;
		for(Object item: queryResults) {
			Object[] theItem = (Object[]) item;
			int year = (Integer) theItem[0];
			dataMap.put(year, (Long) theItem[1]);
			
			minYear = Math.min(minYear, year);
			maxYear = Math.max(maxYear, year);
		}
		
		// Convert the result to the report row format
		List<ReportRow> results = new ArrayList<ReportRow>();
		for(int year=maxYear; year>=minYear; year--) {
			Long count = dataMap.get(year);
			if (count==null) { count = 0L; }
			
			results.add(new ReportRow(
				new ReportValue(YEAR_COLUMN, (long) year),
				new ReportValue(NUMBER_COLUMN, count)
			));
		}
		
		return results;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
