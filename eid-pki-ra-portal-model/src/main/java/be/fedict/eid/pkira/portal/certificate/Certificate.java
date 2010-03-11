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
package be.fedict.eid.pkira.portal.certificate;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.generated.privatews.CertificateWS;

@Entity
@Name("tempcertificate")
@Scope(ScopeType.CONVERSATION)
public class Certificate extends be.fedict.eid.pkira.portal.domain.Certificate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7345021688719488185L;
	private int id;
	private String serialNumber;
	private String subject;
	private Date validityStart;
	private Date validityEnd;
	private String requesterName;
	private String issuer;
//	private String certificateType;

	public Certificate(){
	}
	
	public Certificate(CertificateWS certificatews){
		 
		setSerialNumber(certificatews.getSerialNumber());
		setSubject(certificatews.getSubject());

		if(certificatews.getValidityStart() != null){
			setValidityStart(certificatews.getValidityStart().toGregorianCalendar().getTime());
		}
		if(certificatews.getValidityEnd() != null){
			setValidityEnd(certificatews.getValidityEnd().toGregorianCalendar().getTime());
		}
		setRequesterName(certificatews.getRequesterName());
		setIssuer(certificatews.getIssuer());
//		setCertificateType(certificatews.getCertificateType());
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the validityStart
	 */
	public Date getValidityStart() {
		return validityStart;
	}

	/**
	 * @param validityStart the validityStart to set
	 */
	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}

	/**
	 * @return the validityEnd
	 */
	public Date getValidityEnd() {
		return validityEnd;
	}

	/**
	 * @param validityEnd the validityEnd to set
	 */
	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}

	/**
	 * @return the requesterName
	 */
	public String getRequesterName() {
		return requesterName;
	}

	/**
	 * @param requesterName the requesterName to set
	 */
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

//	public void setCertificateType(String certificateType) {
//		this.certificateType = certificateType;
//	}

//	public String getCertificateType() {
//		return certificateType;
//	}
}
