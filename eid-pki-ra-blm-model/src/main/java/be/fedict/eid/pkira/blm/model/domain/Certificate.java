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
package be.fedict.eid.pkira.blm.model.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.crypto.CertificateInfo;

@Entity
@Name(Certificate.NAME)
public class Certificate implements Serializable {

	public static final String NAME = "certificate";

	private static final long serialVersionUID = -6539022465744360747L;

	private BigInteger serialNumber;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String x509;
	private String subject;
	private Date validityStart;
	private Date validityEnd;
	private String requesterName;
	private String issuer;

	// @ManyToOne(optional = true)
	// private CertificateDomain certificateDomain;

	public Certificate() {
	}

	public Certificate(BigInteger serialNumber, String x509, String subject, Date validityStart, Date validityEnd,
			String requesterName, String issuer) {
		this.serialNumber = serialNumber;
		this.x509 = x509;
		this.subject = subject;
		this.validityStart = validityStart;
		this.validityEnd = validityEnd;
		this.requesterName = requesterName;
		this.issuer = issuer;
	}

	public Certificate(String x509, CertificateInfo certificateInfo, String requesterName) {
		this.x509 = x509;
		this.serialNumber = certificateInfo.getSerialNumber();
		this.subject = certificateInfo.getSubject();
		this.validityStart = certificateInfo.getValidityStart();
		this.validityEnd = certificateInfo.getValidityEnd();
		this.issuer = certificateInfo.getIssuer();
		this.requesterName = requesterName;
	}

	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
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
		return x509;
	}

	/**
	 * @param x509
	 *            the x509 to set
	 */
	public void setX509(String x509) {
		this.x509 = x509;
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
	 * @param requesterName
	 *            the requesterName to set
	 */
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	// public void setCertificateDomain(CertificateDomain certificateDomain) {
	// this.certificateDomain = certificateDomain;
	// }
	//
	// public CertificateDomain getCertificateDomain() {
	// return certificateDomain;
	// }

}
