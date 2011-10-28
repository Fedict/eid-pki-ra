/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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
package be.fedict.eid.pkira.blm.model.contracts;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import be.fedict.eid.pkira.common.util.FilterHelper;

@Name(CertificateListHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class CertificateListHandlerBean implements CertificateListHandler {

	private static final long serialVersionUID = -5017092109045531175L;

	private static final String CERTIFICATES_NAME = "certificates";

	@DataModel(scope=ScopeType.PAGE)
	private List<Certificate> certificates;

	private Integer certificateDomainId;

	@In(value = CertificateQuery.NAME, create = true)
	private CertificateQuery certificateQuery;

	private String dnFilterValue;

	@In(value=FilterHelper.NAME, create = true)
	private FilterHelper expressionMatcher;
	
	@In(value = FilterHelper.NAME, create = true)
	private FilterHelper filterHelper;

	private Date startDateFrom, startDateTo, endDateFrom, endDateTo;

	@Override
	public List<Certificate> findCertificateList() {
		certificates = certificateQuery.getFindCertificates(certificateDomainId);
		return certificates;
	}

	@Factory(CERTIFICATES_NAME)
	public void initCertificateList() {
		findCertificateList();
	}
	
	@Override
	public boolean filterByDN(Object current) {
		if (StringUtils.isBlank(dnFilterValue)) {
			return true;
		}
		Certificate certificate = (Certificate) current;
		return expressionMatcher.matchWildcardExpression(dnFilterValue + "*", certificate.getDistinguishedName());
	}
	
	@Override
	public boolean filterByStartDate(Object current) {
		Certificate certificate = (Certificate) current;

		return filterHelper.filterByDate(certificate.getValidityStart(), startDateFrom, startDateTo);
	}
	
	@Override
	public boolean filterByEndDate(Object current) {
		Certificate certificate = (Certificate) current;

		return filterHelper.filterByDate(certificate.getValidityEnd(), endDateFrom, endDateTo);
	}

	public void setCertificateDomainId(Integer certificateDomainId) {
		this.certificateDomainId = certificateDomainId;
		initCertificateList();
	}

	public Integer getCertificateDomainId() {
		return certificateDomainId;
	}

	public String getDnFilterValue() {
		return dnFilterValue;
	}

	public void setDnFilterValue(String dnFilterValue) {
		this.dnFilterValue = dnFilterValue;
	}

	public FilterHelper getExpressionMatcher() {
		return expressionMatcher;
	}

	public void setExpressionMatcher(FilterHelper expressionMatcher) {
		this.expressionMatcher = expressionMatcher;
	}

	
	public Date getStartDateFrom() {
		return startDateFrom;
	}

	
	public void setStartDateFrom(Date startDateFrom) {
		this.startDateFrom = startDateFrom;
	}

	
	public Date getStartDateTo() {
		return startDateTo;
	}

	
	public void setStartDateTo(Date startDateTo) {
		this.startDateTo = startDateTo;
	}

	
	public void setFilterHelper(FilterHelper filterHelper) {
		this.filterHelper = filterHelper;
	}

	
	public Date getEndDateFrom() {
		return endDateFrom;
	}

	
	public void setEndDateFrom(Date endDateFrom) {
		this.endDateFrom = endDateFrom;
	}

	
	public Date getEndDateTo() {
		return endDateTo;
	}

	
	public void setEndDateTo(Date endDateTo) {
		this.endDateTo = endDateTo;
	}

}
