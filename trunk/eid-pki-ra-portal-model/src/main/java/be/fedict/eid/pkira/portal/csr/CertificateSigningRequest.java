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
package be.fedict.eid.pkira.portal.csr;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.util.Base64;

import be.fedict.eid.pkira.crypto.CSRInfo;

/**
 * @author Bram Baeyens
 */
@Name("certificateSigningRequest")
@Scope(ScopeType.CONVERSATION)
public class CertificateSigningRequest implements Serializable {

	private static final long serialVersionUID = 4134577004436570408L;
	
	private CSRInfo distinguishedName;
	private CertificateType certificateType;
	private ValidityPeriod validityPeriod;
	private String operatorName;
	private String operatorFunction;
	private String operatorPhone;
	private String operatorEmail; 
	private byte[] csr;
	private String contentType;
	private String description;
	
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

	public ValidityPeriod getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(ValidityPeriod validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorFunction() {
		return operatorFunction;
	}

	public void setOperatorFunction(String operatorFunction) {
		this.operatorFunction = operatorFunction;
	}

	public String getOperatorPhone() {
		return operatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}

	public String getOperatorEmail() {
		return operatorEmail;
	}

	public void setOperatorEmail(String operatorEmail) {
		this.operatorEmail = operatorEmail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getCsr() {
		return csr;
	}
	
	public void setCsr(byte[] csr) {
		this.csr = csr;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
