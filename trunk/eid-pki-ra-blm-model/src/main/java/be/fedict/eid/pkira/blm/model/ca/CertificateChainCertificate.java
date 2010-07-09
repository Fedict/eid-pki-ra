package be.fedict.eid.pkira.blm.model.ca;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@Column(name = "CHAIN_CERT_ID", nullable = false, unique = true)
	private Integer id;

	@Lob
	@Column(name = "DATA", nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private byte[] certificateData;

	@Column(name = "SUBJECT", nullable = false)
	private String subject;

	@JoinColumn(name = "ISSUER")
	@ManyToOne
	private CertificateChainCertificate issuer;

	@JoinColumn(name = "CHAIN_ID", nullable=true)
	@ManyToOne(optional=true)
	private CertificateChain certificateChain;

	@Column(name = "SERIALNUMBER", nullable = false)
	private BigInteger serialNumber;

	public byte[] getCertificateData() {
		return certificateData;
	}

	public void setCertificateData(byte[] certificateData) {
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

	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
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
