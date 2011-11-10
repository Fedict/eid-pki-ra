package be.fedict.eid.pkira.blm.model.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportColumn;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportRow;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportValue;

public abstract class KeyValueReportGenerator<KeyType, ValueType> {

	@PersistenceContext
	private EntityManager entityManager;

	public final List<ReportColumn> getReportColumns() {
		return Arrays.asList(getKeyColumn(), getValueColumn());
	}

	public final List<ReportRow> getReport() {
		// Execute the query
		Query query = entityManager.createQuery(getQueryString());
		setQueryParameters(query);
		List<?> queryResults = query.getResultList();
		
		// Convert the result to a map, keeping track of start and end dates
		Map<KeyType, ValueType> dataMap = new HashMap<KeyType, ValueType>();
		for(Object item: queryResults) {
			Object[] theItem = (Object[]) item;
			dataMap.put(getKeyFromDataRow(theItem), getValueFromDataRow(theItem));
		}
		
		// Convert the result to the report row format
		KeyType minValue = getMinValue(dataMap.keySet());
		KeyType maxValue = getMaxValue(dataMap.keySet());
		Iterator<KeyType> iterator = getIterator(maxValue, minValue);
		
		List<ReportRow> results = new ArrayList<ReportRow>();
		while(iterator.hasNext()) {
			KeyType key = iterator.next();
			ValueType value = dataMap.get(key);
			if (value==null) { value = getZeroValue(); }
			
			results.add(new ReportRow(
				new ReportValue(getKeyColumn(), key),
				new ReportValue(getValueColumn(), value)
			));
		}
		
		return results;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected abstract String getQueryString();

	protected abstract void setQueryParameters(Query query);

	protected abstract KeyType getKeyFromDataRow(Object[] theItem);

	protected abstract ValueType getValueFromDataRow(Object[] theItem);

	protected abstract KeyType getMinValue(Collection<KeyType> values);

	protected abstract KeyType getMaxValue(Collection<KeyType> values);

	protected abstract Iterator<KeyType> getIterator(KeyType maxValue, KeyType minValue);

	protected abstract ValueType getZeroValue();

	protected abstract ReportColumn getKeyColumn();

	protected abstract ReportColumn getValueColumn();

}
