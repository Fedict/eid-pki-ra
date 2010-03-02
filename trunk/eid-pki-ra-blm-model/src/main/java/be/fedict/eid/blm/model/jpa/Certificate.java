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
package be.fedict.eid.blm.model.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;


@Entity
public class Certificate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6539022465744360747L;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String X509;
	private String subject;
	private Date validityStart;
	private Date validityEnd;
	private String requesterName;
	private String issuer;

//	@ManyToOne(optional = true)
//	private CertificateDomain certificateDomain;

	public Certificate() {
	}

	public Certificate(String x509, String subject, Date validityStart,
			Date validityEnd, String requesterName, String issuer) {
		super();
		this.X509 = x509;
		this.subject = subject;
		this.validityStart = validityStart;
		this.validityEnd = validityEnd;
		this.requesterName = requesterName;
		this.issuer = issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getIssuer() {
		return issuer;
	}



	/**
	 * @return the x509
	 */
	public String getX509() {
		return X509;
	}

	/**
	 * @param x509
	 *            the x509 to set
	 */
	public void setX509(String x509) {
		X509 = x509;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
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
	 * @param validityStart
	 *            the validityStart to set
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
	 * @param validityEnd
	 *            the validityEnd to set
	 */
	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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

//	public void setCertificateDomain(CertificateDomain certificateDomain) {
//		this.certificateDomain = certificateDomain;
//	}
//
//	public CertificateDomain getCertificateDomain() {
//		return certificateDomain;
//	}

}
