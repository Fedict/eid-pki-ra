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

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Index;

/**
 * A line in a report.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@Table(name = "REPORT_ENTRY")
@NamedQueries(
	{
			@NamedQuery(name = "getReportData", query = "SELECT DISTINCT e FROM ReportEntry e WHERE month=:month"),
			@NamedQuery(name = "getMonths", query = "SELECT DISTINCT e.month FROM ReportEntry e WHERE month>=:startMonth AND month<=:endMonth") })
public class ReportEntry {

	public static enum ContractType {
		REQUEST, REVOCATION
	}

	@Column(name = "CERTIFICATE_AUTHORITY_NAME", nullable = false, updatable = false)
	private String certificateAuthorityName;

	@Column(name = "CERTIFICATE_DOMAIN_NAME", nullable = false, updatable = false)
	private String certificateDomainName;

	@Enumerated(EnumType.STRING)
	@Column(name = "CONTRACT_TYPE", updatable = false)
	private ContractType contractType;

	@Id
	@GeneratedValue
	@Column(name = "REPORT_ENTRY_ID", nullable = false, updatable = false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Version
	@Column(name = "LOG_TIME", updatable = false)
	private Date logTime;

	@Column(name = "MONTH", nullable = false, updatable = false)
	@Index(name = "idxReportEntryMonth")
	private String month;

	@Column(name = "REQUESTER", nullable = false, updatable = false)
	private String requester;

	@Column(name = "SUBJECT", nullable = false, updatable = false)
	private String subject;

	@Column(name = "SUCCESS", nullable = false, updatable = false)
	private boolean success;

	public String getCertificateAuthorityName() {
		return certificateAuthorityName;
	}

	public String getCertificateDomainName() {
		return certificateDomainName;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public Integer getId() {
		return id;
	}

	public Date getLogTime() {
		return logTime;
	}

	public String getMonth() {
		return month;
	}

	public String getRequester() {
		return requester;
	}

	public String getSubject() {
		return subject;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setCertificateAuthorityName(String certificateAuthorityName) {
		this.certificateAuthorityName = certificateAuthorityName;
	}

	public void setCertificateDomainName(String certificateDomainName) {
		this.certificateDomainName = certificateDomainName;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	protected void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

}
