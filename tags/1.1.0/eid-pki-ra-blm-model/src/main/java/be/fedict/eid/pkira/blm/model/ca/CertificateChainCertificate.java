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

package be.fedict.eid.pkira.blm.model.ca;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;

/**
 * An element in a certificate chain.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@Table(name = "CERTIFICATE_CHAIN_CERTIFICATE")
public class CertificateChainCertificate implements Serializable, Comparable<CertificateChainCertificate> {

	private static final long serialVersionUID = 5013706260709186252L;

	@Id
	@GeneratedValue
	@Column(name = "CHAIN_CERT_ID", nullable = false)
	private Integer id;

	@Lob
	@Column(name = "DATA", nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String certificateData;

	@Column(name = "SUBJECT", nullable = false)
	private String subject;

	@JoinColumn(name = "ISSUER")
	@ManyToOne
	private CertificateChainCertificate issuer;

	@JoinColumn(name = "CHAIN_ID", nullable=true)
	@ManyToOne(optional=true)
	private CertificateChain certificateChain;

	@Column(name = "SERIALNUMBER", nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String serialNumber;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCertificateData() {
		return certificateData;
	}

	public void setCertificateData(String certificateData) {
		this.certificateData = certificateData;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public CertificateChainCertificate getIssuer() {
		return issuer;
	}

	public void setIssuer(CertificateChainCertificate issuer) {
		this.issuer = issuer;
	}

	public Integer getId() {
		return id;
	}

	public void setCertificateChain(CertificateChain certificateChain) {
		this.certificateChain = certificateChain;
	}

	public CertificateChain getCertificateChain() {
		return certificateChain;
	}

	@Override
	public int compareTo(CertificateChainCertificate other) {
		if (other == null) {
			return 1;
		}
		if (subject == null) {
			return other.subject == null ? 0 : -1;
		}

		return subject.compareTo(other.subject);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CertificateChainCertificate)) {
			return false;
		}

		CertificateChainCertificate other = (CertificateChainCertificate) obj;
		return ObjectUtils.equals(issuer, other.issuer) && ObjectUtils.equals(serialNumber, other.serialNumber)
				&& ObjectUtils.equals(subject, other.subject);
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(issuer) + ObjectUtils.hashCode(serialNumber);
	}
}
