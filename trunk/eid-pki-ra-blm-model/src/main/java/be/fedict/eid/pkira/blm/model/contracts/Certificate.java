/*	  
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CollectionOfElements;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.quartz.SchedulerException;

import be.fedict.eid.pkira.blm.model.ca.CertificateChain;
import be.fedict.eid.pkira.blm.model.ca.CertificateChainCertificate;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.crypto.certificate.CertificateInfo;

@Entity
@Table(name = "CERTIFICATE", uniqueConstraints =
	{ @UniqueConstraint(columnNames =
		{ "ISSUER", "SERIAL_NUMBER" }) })
@Name(Certificate.NAME)
@NamedQueries(
	{ @NamedQuery(name = Certificate.NQ_FIND_CERTIFICATES_BY_CERTIFICATE_DOMAIN_ID, query = "SELECT c FROM Certificate c WHERE c.certificateDomain.id=:certificateDomainId ORDER BY c.validityStart DESC") })
public class Certificate implements Serializable {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificate";

	public static final String NQ_FIND_CERTIFICATES_BY_CERTIFICATE_DOMAIN_ID = "findCertificatesByCertificateDomainId";

	private static final long serialVersionUID = -6539022465744360747L;

	@CollectionOfElements(fetch = FetchType.LAZY)
	@JoinTable(name = "TRIGGERHANDLES")
	private final Set<QuartzTriggerHandle> timers = new HashSet<QuartzTriggerHandle>();

	@Id
	@GeneratedValue
	@Column(name = "CERTIFICATE_ID", nullable = false)
	private Integer id;
	@Column(name = "SERIAL_NUMBER", nullable = false, precision = 38, scale = 0)
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
	@JoinColumn(name = "CERTIFICATE_DOMAIN_ID", nullable = false)
	private CertificateDomain certificateDomain;

	@ManyToOne(optional = true)
	@JoinColumn(name = "CERTIFICATE_CHAIN_CERTIFICATE", nullable = true)
	private CertificateChainCertificate certificateChainCertificate;

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

		this.certificateChainCertificate = null;
		CertificateChain certificateChain = certificateDomain.getCertificateAuthority().getCertificateChain();
		if (certificateChain != null) {
			if (certificateType == CertificateType.CLIENT) {
				certificateChainCertificate = certificateChain.getClientChain();
			} else if (certificateType == CertificateType.SERVER) {
				certificateChainCertificate = certificateChain.getServerChain();
			} else if (certificateType == CertificateType.CODE) {
				certificateChainCertificate = certificateChain.getCodeSigningChain();
			} else if (certificateType == CertificateType.PERSONS) {
				certificateChainCertificate = certificateChain.getPersonsChain();
			}
		}
	}

	/**
	 * Creates a zip file containing the certificate and its chain.
	 */
	public byte[] getCertificateZip() {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try {
			ZipOutputStream zipOutput = new ZipOutputStream(byteOutput);

			// Add the certificate itself
			String fileName = "certificate.crt";
			String x509 = getX509();
			addEntryToZip(zipOutput, fileName, x509);

			// Add the certificates in the certificate chain
			int number = 1;
			for (CertificateChainCertificate chain = getCertificateChainCertificate(); chain != null; chain = chain
					.getIssuer()) {
				addEntryToZip(zipOutput, "chain" + (number++) + ".crt", chain.getCertificateData());
			}

			zipOutput.close();
		} catch (IOException e) {
			// Unexpected!
			throw new RuntimeException(e);
		}
		return byteOutput.toByteArray();
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
		if (getTimers() != null) {
			for (QuartzTriggerHandle timer : getTimers()) {
				timer.cancel();
			}
		}
		timers.clear();
	}

	public Set<QuartzTriggerHandle> getTimers() {
		return timers;
	}

	public void addTimer(QuartzTriggerHandle timer) {
		this.timers.add(timer);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCertificateChainCertificate(CertificateChainCertificate certificateChainCertificate) {
		this.certificateChainCertificate = certificateChainCertificate;
	}

	public CertificateChainCertificate getCertificateChainCertificate() {
		return certificateChainCertificate;
	}
	
	private void addEntryToZip(ZipOutputStream zipOutput, String fileName, String x509) throws IOException {
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOutput.putNextEntry(zipEntry);
		zipOutput.write(x509.getBytes());
		zipOutput.closeEntry();
	}
}
