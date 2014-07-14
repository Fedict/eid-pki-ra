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
package be.fedict.eid.pkira.blm.model.reporting;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
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

	private Date reportDate = new Date();
    private ReportEntry.ContractType contractTypeFilter;
    private String certificateDomainNameFilter;
    private String subjectFilter;
    private Boolean successFilter;

    public ReportEntryListQuery() {
        determineEjbql();
    }

    private void determineEjbql() {
        StringBuilder queryBuilder = new StringBuilder("SELECT e FROM ReportEntry e WHERE logTime BETWEEN :start AND :end");
        if (contractTypeFilter!=null) queryBuilder.append(" AND contractType=:contractType");
        if (StringUtils.isNotBlank(certificateDomainNameFilter)) queryBuilder.append(" AND lower(certificateDomainName) LIKE :certificateDomainName");
        if (StringUtils.isNotBlank(subjectFilter)) queryBuilder.append(" AND lower(subject) LIKE :subject");
        if (successFilter!=null) queryBuilder.append(" AND success=:success");

        setEjbql(queryBuilder.toString());
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

    public String getCertificateDomainNameFilter() {
        return certificateDomainNameFilter;
    }

    public void setCertificateDomainNameFilter(String certificateDomainNameFilter) {
        this.certificateDomainNameFilter = certificateDomainNameFilter;
    }

    public List<ReportEntry.ContractType> getContractTypes() {
        return Arrays.asList(ReportEntry.ContractType.values());
    }

    public ReportEntry.ContractType getContractTypeFilter() {
        return contractTypeFilter;
    }

    public void setContractTypeFilter(ReportEntry.ContractType contractTypeFilter) {
        this.contractTypeFilter = contractTypeFilter;
    }

    public String getSubjectFilter() {
        return subjectFilter;
    }

    public void setSubjectFilter(String subjectFilter) {
        this.subjectFilter = subjectFilter;
    }

    public Boolean getSuccessFilter() {
        return successFilter;
    }

    public void setSuccessFilter(Boolean successFilter) {
        this.successFilter = successFilter;
    }

    public List<Boolean> getSuccessValues() {
        return Arrays.asList(true, false);
    }

    public void filterChanged() {
        determineEjbql();
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

        if (contractTypeFilter!=null) query.setParameter("contractType", contractTypeFilter);
        if (StringUtils.isNotBlank(certificateDomainNameFilter)) query.setParameter("certificateDomainName", "%" + certificateDomainNameFilter.toLowerCase() + "%");
        if (StringUtils.isNotBlank(subjectFilter)) query.setParameter("subject", "%" + subjectFilter.toLowerCase() + "%");
        if (successFilter!=null) query.setParameter("success", successFilter);
	}

	@Override
	protected Query createCountQuery() {
		Query query = super.createCountQuery();
		setQueryParameters(query);
		return query;
	}

}
