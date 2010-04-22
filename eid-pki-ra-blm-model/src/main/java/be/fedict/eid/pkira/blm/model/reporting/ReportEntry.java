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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Index;

import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;

/**
 * A line in a report.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@Table(name = "REPORT_ENTRY")
public class ReportEntry {

	public static enum ContractType {
		REQUEST, REVOKE
	}

	@Column(name = "CERTIFICATE_AUTHORITY_NAME", nullable = false, updatable = false)
	private String certificateAuthorityName;

	@Column(name = "CERTIFICATE_DOMAIN_NAME", nullable = false, updatable = false)
	private String certificateDomainName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID", updatable = false)
	private AbstractContract contract;

	@Enumerated(EnumType.STRING)
	@Column(name = "CONTRACT_TYPE", updatable = false)
	private ContractType contractType;

	@Id
	@GeneratedValue
	@Column(name = "REPORT_ENTRY_ID", nullable = false, unique = true, updatable = false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Version
	@Column(name = "LOG_TIME", updatable = false)
	private Date logTime;

	@Column(name = "MONTH", nullable = false, updatable = false)
	@Index(name = "idxReportEntryMonth")
	private String month;

	@Column(name = "SUCCESS", nullable = false, updatable = false)
	private boolean success;

	public String getCertificateAuthorityName() {
		return certificateAuthorityName;
	}

	public String getCertificateDomainName() {
		return certificateDomainName;
	}

	public AbstractContract getContract() {
		return contract;
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

	public boolean isSuccess() {
		return success;
	}

	public void setCertificateAuthorityName(String certificateAuthorityName) {
		this.certificateAuthorityName = certificateAuthorityName;
	}

	public void setCertificateDomainName(String certificateDomainName) {
		this.certificateDomainName = certificateDomainName;
	}

	public void setContract(AbstractContract contract) {
		this.contract = contract;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
