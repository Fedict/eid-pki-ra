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

package be.fedict.eid.pkira.blm.model.xkmslog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

/**
 * @author Jan Van den Bergh
 *
 */
@Name(XKMSLogEntryQuery.NAME)
@Scope(ScopeType.CONVERSATION)
public class XKMSLogEntryQuery extends EntityQuery<XKMSLogEntry> {

	private static final long serialVersionUID = -1687988818281539366L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.xkmsLogEntryQuery";

    @In(value = XKMSLogEntryHome.NAME, create = true)
    private XKMSLogEntryHome xkmsLogEntryHome;

    private Date reportDate = new Date();

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


    @Override
	public String getEjbql() {
		return "select xkmsLogEntry from XKMSLogEntry xkmsLogEntry where creationDate between :start and :end";
	}

    public void downloadRequest(Integer id) {
        xkmsLogEntryHome.setId(id);
        xkmsLogEntryHome.downloadRequest();
    }

    public void downloadResponse(Integer id) {
        xkmsLogEntryHome.setId(id);
        xkmsLogEntryHome.downloadResponse();
    }
}
