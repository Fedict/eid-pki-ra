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

package be.fedict.eid.pkira.portal.domain;

import java.io.Serializable;

import org.jboss.seam.annotations.In;

import be.fedict.eid.pkira.crypto.CSRInfo;

/**
 * @author Bram Baeyens
 *
 */
public abstract class Certificate implements Serializable {
	
	private static final long serialVersionUID = -4191567270718469935L;
	
	private CSRInfo distinguishedName;
	private CertificateType certificateType;
	@In(create=true)
	private Operator operator;
	private String description;
	private String legalNotice = "testLegalNotice";
	private String base64Csr;
	
	public CSRInfo getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(CSRInfo distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public CertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLegalNotice() {
		return legalNotice;
	}

	public void setLegalNotice(String legalNotice) {
		this.legalNotice = legalNotice;
	}
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getBase64Csr() {
		return base64Csr;
	}

	public void setBase64Csr(String base64Csr) {
		this.base64Csr = base64Csr;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("distinguishedName=").append(distinguishedName)
			.append(", certificateType=").append(certificateType)
			.append(", operator=").append(operator)
			.append(", legalNotice=").append(legalNotice)
			.toString();
	}
}
