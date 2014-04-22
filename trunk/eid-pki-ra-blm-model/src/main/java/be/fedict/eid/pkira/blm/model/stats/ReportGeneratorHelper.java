/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

package be.fedict.eid.pkira.blm.model.stats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportColumn;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportRow;
import be.fedict.eid.pkira.blm.model.stats.StatisticsReportGenerator.ReportValue;

public abstract class ReportGeneratorHelper {

	@PersistenceContext
	private EntityManager entityManager;

	public final List<ReportRow> getReport() {
		// Execute the query
		Query query = entityManager.createQuery(getQueryString());
		setQueryParameters(query);
		List<?> queryResults = query.getResultList();

		// Convert the result to a map, keeping track of start and end dates
		List<ReportRow> result = new ArrayList<ReportRow>();
		for(Object queryResult: queryResults) {
			Object[] items = (Object[]) queryResult;
			List<Object> values = getValuesFromDataRow(items);
			ReportRow row = createReportRow(values);
			
			result.add(row);
		}
		
		return result;
	}

	protected ReportRow createReportRow(List<Object> values) {
		List<ReportValue> reportValues = new ArrayList<StatisticsReportGenerator.ReportValue>();
		List<ReportColumn> columns = getReportColumns();
		for (int i = 0; i < values.size(); i++) {
			reportValues.add(new ReportValue(columns.get(i), values.get(i)));
		}

		return new ReportRow(reportValues);
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected final static Date createDate(int day, int month, int year) {
		GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	protected abstract String getQueryString();

	protected abstract void setQueryParameters(Query query);

	protected abstract List<Object> getValuesFromDataRow(Object[] items);

	public abstract List<ReportColumn> getReportColumns();

	protected final Date getYearFromStartOfDataRow(Object[] items) {
		int year = (Integer) items[0];
		Date date = createDate(1, 1, year);
		return date;
	}

	protected Date getMonthFromStartOfDataRow(Object[] items) {
		int year = (Integer) items[0];
		int month = (Integer) items[1];
		Date createDate = createDate(1, month, year);
		return createDate;
	}

	protected Date getDayFromStartOfDataRow(Object[] items) {
		int year = (Integer) items[0];
		int month = (Integer) items[1];
		int day = (Integer) items[2];
		Date createDate = createDate(day, month, year);
		return createDate;
	}
}
