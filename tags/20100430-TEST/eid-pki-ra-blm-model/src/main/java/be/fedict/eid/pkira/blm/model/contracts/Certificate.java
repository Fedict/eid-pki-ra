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
package be.fedict.eid.pkira.blm.model.contracts;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.quartz.SchedulerException;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.crypto.CertificateInfo;

@Entity
@Name(Certificate.NAME)
public class Certificate implements Serializable {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificate";

	private static final long serialVersionUID = -6539022465744360747L;

	@Column(name="TRIGGERHANDLE", nullable=true)
	private QuartzTriggerHandle timer;
	
	@Id
	@GeneratedValue
	@Column(name = "CERTIFICATE_ID", nullable = false, unique = true)
	private Integer id;

	@Column(name = "SERIAL_NUMBER", nullable = false)
	private BigInteger serialNumber;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "X509", nullable = false)
	private String x509;

	@Column(name = "DISTINGUISHED_NAME", nullable = false)
	private String distinguishedName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VALIDITY_START", nullable = false)
	private Date validityStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VALIDITY_END", nullable = false)
	private Date validityEnd;

	@Column(name = "REQUESTER", nullable = false)
	private String requesterName;

	@Column(name = "ISSUER", nullable = false)
	private String issuer;

	@Enumerated(EnumType.STRING)
	@Column(name = "CERTIFICATE_TYPE", nullable = false)
	private CertificateType certificateType;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT")
	private CertificateSigningContract contract;

	@ManyToOne(optional = false)
	@JoinColumn(name="CERTIFICATE_DOMAIN_ID", nullable=false)
	private CertificateDomain certificateDomain;

	public Certificate() {
	}

	public Certificate(String x509, CertificateInfo certificateInfo, String requesterName,
			CertificateSigningContract contract) {
		this.x509 = x509;
		this.serialNumber = certificateInfo.getSerialNumber();
		this.distinguishedName = certificateInfo.getDistinguishedName();
		this.validityStart = certificateInfo.getValidityStart();
		this.validityEnd = certificateInfo.getValidityEnd();
		this.issuer = certificateInfo.getIssuer();
		this.certificateType = contract.getCertificateType();
		this.requesterName = requesterName;
		this.contract = contract;
		this.certificateDomain = contract.getCertificateDomain();
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
	public String getDistinguishedName() {
		return distinguishedName;
	}

	/**
	 * @param distinguishedName
	 *            the distinguishedName to set
	 */
	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
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

	public Integer getId() {
		return id;
	}

	public CertificateSigningContract getContract() {
		return contract;
	}

	public void setContract(CertificateSigningContract contract) {
		this.contract = contract;
	}

	public CertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}

	
	public CertificateDomain getCertificateDomain() {
		return certificateDomain;
	}

	
	public void setCertificateDomain(CertificateDomain certificateDomain) {
		this.certificateDomain = certificateDomain;
	}

	public void cancelNotificationMail() throws SchedulerException {
		if(timer != null){
			timer.cancel();
		}
	}

	
	public QuartzTriggerHandle getTimer() {
		return timer;
	}

	
	public void setTimer(QuartzTriggerHandle timer) {
		this.timer = timer;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

