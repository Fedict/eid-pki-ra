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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.reporting.ReportEntry.ContractType;
import be.fedict.eid.pkira.reports.MonthlyReportBuilder;
import be.fedict.eid.pkira.reports.ReportBuilder;
import be.fedict.eid.pkira.reports.ReportsClient;

/**
 * @author Jan Van den Bergh
 */
@Stateless
@Name(ReportManager.NAME)
public class ReportManagerBean implements ReportManager {

	@In(value = ReportEntryHome.NAME, create = true)
	private ReportEntryHome reportEntryHome;

	@In(value = "be.fedict.eid.pkira.blm.reportsClient", create = true)
	private ReportsClient reportsClient;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void addLineToReport(AbstractContract contract, boolean success) {
		ReportEntry entry = reportEntryHome.getInstance();
		entry.setCertificateAuthorityName(contract.getCertificateDomain().getCertificateAuthority().getName());
		entry.setCertificateDomainName(contract.getCertificateDomain().getName());
		entry.setContract(contract);
		entry.setSuccess(success);
		entry.setMonth(new SimpleDateFormat("yyyy-MM").format(new Date()));
		entry.setContractType(mapToContractType(contract));

		reportEntryHome.persist();
	}

	/**
	 * {@inheritDoc}
	 */
	public String generateReport(String monthYear, boolean showCertificateAuthorities, boolean showCertificateDomains) {
		// Basic report builder
		ReportBuilder reportBuilder = new ReportBuilder();
		MonthlyReportBuilder monthlyReportBuilder = reportBuilder.newMonthlyReport().setDate(monthYear);

		// Get CA data
		if (showCertificateAuthorities) {
			addCertificateAuthorityReports(monthlyReportBuilder, monthYear);
		}
		
		// Get CD data
		if (showCertificateDomains) {
			addCertificateDomainReports(monthlyReportBuilder, monthYear);
		}

		// Convert to XML
		return reportsClient.marshalReport(reportBuilder.toXmlType());
	}

	@SuppressWarnings("unchecked")
	private void addCertificateAuthorityReports(MonthlyReportBuilder monthlyReportBuilder, String monthYear) {
		// Do the query
		List<AggregateData> list = entityManager.createNamedQuery(
				"getCertificateAuthorityAggregateData").setParameter("month", monthYear).getResultList();

		// Get the CA names
		Set<String> caNames = new TreeSet<String>();
		for (AggregateData item : list) {
			caNames.add(item.getName());
		}

		// Create the report elements
		for (String caName : caNames) {
			int requestSuccessCount = 0;
			int requestFailureCount = 0;
			int revokeSuccessCount = 0;
			int revokeFailureCount = 0;

			for (AggregateData item : list) {
				if (StringUtils.equals(item.getName(), caName)) {
					if (item.getContractType() == ContractType.REQUEST) {
						if (item.isSuccess()) {
							requestSuccessCount = item.getCount();
						} else {
							requestFailureCount = item.getCount();
						}
					} else {
						if (item.isSuccess()) {
							revokeSuccessCount = item.getCount();
						} else {
							revokeFailureCount = item.getCount();
						}
					}
				}
			}

			monthlyReportBuilder.newCertificateAuthorityReportBuilder().setCertificateAuthorityName(caName)
					.setSigningRequestCounts(requestSuccessCount, requestFailureCount).setRevocationRequestCounts(
							revokeSuccessCount, revokeFailureCount);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addCertificateDomainReports(MonthlyReportBuilder monthlyReportBuilder, String monthYear) {
		// Do the query
		List<AggregateData> list = entityManager.createNamedQuery(
				"getCertificateDomainAggregateData").setParameter("month", monthYear).getResultList();

		// Get the CA names
		Set<String> cdNames = new TreeSet<String>();
		for (AggregateData item : list) {
			cdNames.add(item.getName());
		}

		// Create the report elements
		for (String cdName : cdNames) {
			int requestSuccessCount = 0;
			int requestFailureCount = 0;

			for (AggregateData item : list) {
				if (StringUtils.equals(item.getName(), cdName)) {
					if (item.getContractType() == ContractType.REQUEST) {
						if (item.isSuccess()) {
							requestSuccessCount = item.getCount();
						} else {
							requestFailureCount = item.getCount();
						}
					}
				}
			}

			monthlyReportBuilder.newCertificateDomainReportBuilder().setCertificateDomainName(cdName)
					.setSigningRequestCounts(requestSuccessCount, requestFailureCount);
		}
	}

	private ContractType mapToContractType(AbstractContract contract) {
		if (contract instanceof CertificateSigningContract) {
			return ContractType.REQUEST;
		}
		if (contract instanceof CertificateRevocationContract) {
			return ContractType.REVOKE;
		}

		throw new RuntimeException("Invalid contract type: " + contract.getClass().getName());
	}

	protected void setReportEntryHome(ReportEntryHome reportEntryHome) {
		this.reportEntryHome = reportEntryHome;
	}

	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected void setReportsClient(ReportsClient reportsClient) {
		this.reportsClient = reportsClient;
	}
}
