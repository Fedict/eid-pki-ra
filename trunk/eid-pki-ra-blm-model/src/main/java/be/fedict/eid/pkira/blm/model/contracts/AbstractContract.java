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
package be.fedict.eid.pkira.blm.model.contracts;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.validator.NotNull;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * A received contract document.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@Table(name = "CONTRACT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 16)
@NamedQueries(
	{
		@NamedQuery(name = AbstractContract.NQ_FIND_CONTRACTS_BY_USER_RRN, 
				query = "SELECT contract FROM AbstractContract contract, Registration registration WHERE registration.requester.nationalRegisterNumber = :nationalRegisterNumber AND registration.status = :registrationStatus AND registration.certificateDomain = contract.certificateDomain ORDER BY contract.creationDate DESC"),
		@NamedQuery(name = AbstractContract.NQ_FIND_CONTRACTS_BY_USER_RRN_AND_CERTIFICATE_DOMAIN_ID, 
				query = "SELECT contract FROM AbstractContract contract, Registration registration WHERE registration.requester.nationalRegisterNumber = :nationalRegisterNumber AND registration.status = :registrationStatus AND registration.certificateDomain = contract.certificateDomain AND contract.certificateDomain.id = :certificateDomainId ORDER BY contract.creationDate DESC"),
		@NamedQuery(name = AbstractContract.NQ_FIND_CONTRACTS_BY_CERTIFICATE_DOMAIN_ID, 
				query = "SELECT contract FROM AbstractContract contract WHERE contract.certificateDomain.id = :certificateDomainId ORDER BY contract.creationDate DESC")
	}
)
public abstract class AbstractContract implements Serializable {

	private static final long serialVersionUID = -5082287440865809644L;

	public static final String NQ_FIND_CONTRACTS_BY_USER_RRN = "findContractsByUserRrn";

	public static final String NQ_FIND_CONTRACTS_BY_USER_RRN_AND_CERTIFICATE_DOMAIN_ID = "findContractsByUserRrnAndCertificateDomainId";
	
	public static final String NQ_FIND_CONTRACTS_BY_CERTIFICATE_DOMAIN_ID = "findContractsByDomainId";

	@Id
	@GeneratedValue
	@Column(name = "CONTRACT_ID", nullable = false)
	private Integer id;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTRACT_DOCUMENT", nullable = false)
	private String contractDocument;

	@Column(name = "SUBJECT", nullable = false)
	private String subject;

	@Column(name = "REQUESTER", nullable = false)
	private String requester;

	@ManyToOne(optional = false)
	@JoinColumn(name = "CERTIFICATE_DOMAIN_ID", nullable = false)
	private CertificateDomain certificateDomain;
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Column(name = "CREATION_DATE", nullable = false)
	private Date creationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "RESULT")
	private ResultType result;
	
	@Column(name="RESULT_MESSAGE")
	@Lob
	private String resultMessage;

	public String getContractDocument() {
		return contractDocument;
	}

	public void setContractDocument(String contractDocument) {
		this.contractDocument = contractDocument;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public Integer getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(getClass().getSimpleName());
		builder.append(":\n");		
		appendFields(builder);		
		builder.append(']');		
		return builder.toString();
	}

	public CertificateDomain getCertificateDomain() {
		return certificateDomain;
	}

	public void setCertificateDomain(CertificateDomain certificateDomain) {
		this.certificateDomain = certificateDomain;
	}

	
	public ResultType getResult() {
		return result;
	}

	
	public void setResult(ResultType result) {
		this.result = result;
	}

	
	public String getResultMessage() {
		return resultMessage;
	}

	
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	public abstract String getType();
	
	public abstract CertificateType getCertificateType();

	protected void appendFields(StringBuilder builder) {
		appendField(builder, "Id", getId());
		appendField(builder, "Subject", getSubject());
		appendField(builder, "Requester", getRequester());
		appendField(builder, "Certificate domain", getCertificateDomain().getName());
		appendField(builder, "Contract document", getContractDocument());
	}

	protected void appendField(StringBuilder builder, String name, Object value) {
		builder.append("    ");
		builder.append(name);
		builder.append(" = ");
		builder.append(value);
		builder.append('\n');		
	}
}
