/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.reporting;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

/**
 * Query to get the list of months.
 * 
 * @author Jan Van den Bergh
 */
@Name(ReportEntryListQuery.NAME)
@Scope(ScopeType.CONVERSATION)
public class ReportEntryListQuery extends EntityQuery<ReportEntry> {

	public static final String NAME = "be.fedict.eid.pkira.blm.reportEntryListQuery";

	private static final long serialVersionUID = 1171619330822348557L;

	private Date reportDate = new Date();

	@Override
	public String getEjbql() {
		return "SELECT e FROM ReportEntry e WHERE logTime BETWEEN :start AND :end";
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	public void reportDateChanged() {
		this.refresh();
	}

	@Override
	protected Query createQuery() {
		Query query = super.createQuery();
		setQueryParameters(query);
		return query;
	}

	private void setQueryParameters(Query query) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(reportDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date start = calendar.getTime();

		calendar.add(Calendar.DATE, 1);
		Date end = calendar.getTime();

		query.setParameter("start", start, TemporalType.TIMESTAMP);
		query.setParameter("end", end, TemporalType.TIMESTAMP);
	}

	@Override
	protected Query createCountQuery() {
		Query query = super.createCountQuery();
		setQueryParameters(query);
		return query;
	}

}
