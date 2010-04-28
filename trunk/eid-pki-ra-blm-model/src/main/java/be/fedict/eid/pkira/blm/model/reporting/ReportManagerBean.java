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
import be.fedict.eid.pkira.reports.ReportItemBuilder;
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
		entry.setSubject(contract.getSubject());
		entry.setRequester(contract.getRequester());
		entry.setSuccess(success);
		entry.setMonth(new SimpleDateFormat("yyyy-MM").format(new Date()));
		entry.setContractType(mapToContractType(contract));

		reportEntryHome.persist();
	}

	/**
	 * {@inheritDoc}
	 */
	public String generateReport(String startMonthYear, String endMonthYear, boolean showCertificateAuthorities,
			boolean showCertificateDomains) {
		// Get the months
		List<String> monthYears = getMonthsFromUntil(startMonthYear, endMonthYear);

		// Basic report builder
		ReportBuilder reportBuilder = new ReportBuilder();

		// Add monthly reports
		for (String monthYear : monthYears) {
			// Add subelement
			MonthlyReportBuilder monthlyReportBuilder = reportBuilder.newMonthlyReport().withDate(monthYear);

			// Get the data
			List<ReportEntry> entries = getReportEntriesPerMonth(monthYear);

			// Build CA reports
			if (showCertificateAuthorities) {
				for(final String certificateAuthorityName: getCertificateAuthorityNames(entries)) {
					ReportItemBuilder builder = monthlyReportBuilder.newCertificateAuthorityReportBuilder().withName(certificateAuthorityName);
					fillReportItemBuilder(builder, certificateAuthorityName, entries, new Filter<ReportEntry>() {						
						public boolean accept(ReportEntry item) {
							return StringUtils.equals(certificateAuthorityName, item.getCertificateAuthorityName());
						}
					});
				}
			}

			// Build CD reports
			if (showCertificateDomains) {
				for(final String certificateDomainName: getCertificateDomainNames(entries)) {
					ReportItemBuilder builder = monthlyReportBuilder.newCertificateDomainReportBuilder().withName(certificateDomainName);
					fillReportItemBuilder(builder, certificateDomainName, entries, new Filter<ReportEntry>() {						
						public boolean accept(ReportEntry item) {
							return StringUtils.equals(certificateDomainName, item.getCertificateDomainName());
						}
					});
				}
			}
		}

		// Convert to XML
		return reportsClient.marshalReport(reportBuilder.build());
	}

	@SuppressWarnings("unchecked")
	private List<ReportEntry> getReportEntriesPerMonth(String monthYear) {
		return entityManager.createNamedQuery("getReportData").setParameter("month", monthYear).getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<String> getMonthsFromUntil(String startMonthYear, String endMonthYear) {
		return entityManager.createNamedQuery("getMonths").setParameter("startMonth", startMonthYear).setParameter(
				"endMonth", endMonthYear).getResultList();
	}

	private void fillReportItemBuilder(ReportItemBuilder reportItembuilder, String name, List<ReportEntry> allEntries, Filter<ReportEntry> filter) {
		int signingSuccessCount = 0;
		int signingFailureCount = 0;
		int revokeSuccessCount = 0;
		int revokeFailureCount = 0;
		
		for(ReportEntry entry: allEntries) {
			if (filter.accept(entry)) {
				if (entry.getContractType()==ContractType.REQUEST) {
					if (entry.isSuccess()) {
						signingSuccessCount++;
					} else {
						signingFailureCount++;
					}
				} else if (entry.getContractType()==ContractType.REVOCATION) {
					if (entry.isSuccess()) {
						revokeSuccessCount++;
					} else {
						revokeFailureCount++;
					}
				}			
			
				reportItembuilder.addDetailBuilder()
					.withRequester(entry.getRequester())
					.withSubject(entry.getSubject())
					.withTime(entry.getLogTime())
					.withContractType(mapContractType(entry.getContractType()))
					.withSuccess(entry.isSuccess());
			}
		}
		
		reportItembuilder
			.withName(name)
			.withRequestCounts(signingSuccessCount, signingFailureCount)
			.withRevocationCounts(revokeSuccessCount, revokeFailureCount);
	}
	
	/**
	 * @param contractType
	 * @return
	 */
	private be.fedict.eid.pkira.generated.reports.ContractType mapContractType(ContractType contractType) {
		return Enum.valueOf(be.fedict.eid.pkira.generated.reports.ContractType.class, contractType.name());
	}

	private Set<String> getCertificateAuthorityNames(List<ReportEntry> entries) {
		Set<String> caNames = new TreeSet<String>();
		for (ReportEntry entry: entries) {
			caNames.add(entry.getCertificateAuthorityName());
		}
		return caNames;
	}

	private Set<String> getCertificateDomainNames(List<ReportEntry> entries) {
		Set<String> cdNames = new TreeSet<String>();
		for (ReportEntry entry: entries) {
			cdNames.add(entry.getCertificateDomainName());
		}
		return cdNames;
	}

	private ContractType mapToContractType(AbstractContract contract) {
		if (contract instanceof CertificateSigningContract) {
			return ContractType.REQUEST;
		}
		if (contract instanceof CertificateRevocationContract) {
			return ContractType.REVOCATION;
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
