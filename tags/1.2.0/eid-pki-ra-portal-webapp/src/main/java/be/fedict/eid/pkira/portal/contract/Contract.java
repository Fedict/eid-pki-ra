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

package be.fedict.eid.pkira.portal.contract;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.portal.certificate.CertificateType;

/**
 * @author Bram Baeyens
 *
 */
@Name(Contract.NAME)
public class Contract implements Serializable {

	private static final long serialVersionUID = -3609263677711837474L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.contract";
	
	private Integer id;
	private String dnExpression;
	private String requesterName;
	private String certificateDomainName;
	private Date creationDate;
	private CertificateType certificateType;
	private ContractType contractType;
	private Integer certificateId;
	private String result;
	private String resultMessage;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDnExpression() {
		return dnExpression;
	}
	
	public void setDnExpression(String dnExpression) {
		this.dnExpression = dnExpression;
	}
	
	public String getRequesterName() {
		return requesterName;
	}
	
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}
	
	public String getCertificateDomainName() {
		return certificateDomainName;
	}
	
	public void setCertificateDomainName(String certificateDomainName) {
		this.certificateDomainName = certificateDomainName;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public CertificateType getCertificateType() {
		return certificateType;
	}
	
	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}
	
	public ContractType getContractType() {
		return contractType;
	}
	
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
	
	public Integer getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Integer certificateId) {
		this.certificateId = certificateId;
	}
	
	public boolean isHasCertificate() {
		return certificateId != null;
	}
	
	public String getResult() {
		return result;
	}

	
	public void setResult(String result) {
		this.result = result;
	}

	
	public String getResultMessage() {
		return resultMessage;
	}

	
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
}

