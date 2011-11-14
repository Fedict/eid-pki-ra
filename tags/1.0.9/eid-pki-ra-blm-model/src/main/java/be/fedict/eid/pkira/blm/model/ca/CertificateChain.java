package be.fedict.eid.pkira.blm.model.ca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A certificate chain. It consists of a list of certificates and the
 * certificate in the chain to start with, for client, server and code signing
 * certificates.
 * 
 * @author Jan Van den Bergh
 */
@Entity
@Table(name = "CERTIFICATE_CHAIN")
public class CertificateChain implements Serializable {

	private static final long serialVersionUID = -2834866172241373838L;

	@OneToMany(mappedBy = "certificateChain", cascade = CascadeType.ALL)
	private List<CertificateChainCertificate> certificates;

	@JoinColumn(name = "CLIENT_CHAIN_ID")
	@OneToOne(fetch = FetchType.EAGER)
	private CertificateChainCertificate clientChain;

	@JoinColumn(name = "CODESIGNING_CHAIN_ID")
	@OneToOne(fetch = FetchType.EAGER)
	private CertificateChainCertificate codeSigningChain;

	@Id
	@GeneratedValue
	@Column(name = "CERTIFICATE_CHAIN_ID", nullable = false)
	private Integer id;

	@JoinColumn(name = "SERVER_CHAIN_ID")
	@OneToOne(fetch = FetchType.EAGER)
	private CertificateChainCertificate serverChain;
	
	@JoinColumn(name = "PERSONS_CHAIN_ID")
	@OneToOne(fetch = FetchType.EAGER)
	private CertificateChainCertificate personsChain;

	public List<CertificateChainCertificate> getCertificates() {
		if (certificates == null) {
			certificates = new ArrayList<CertificateChainCertificate>();
		}
		return certificates;
	}

	public CertificateChainCertificate getClientChain() {
		return clientChain;
	}

	public CertificateChainCertificate getCodeSigningChain() {
		return codeSigningChain;
	}

	public Integer getId() {
		return id;
	}

	public CertificateChainCertificate getServerChain() {
		return serverChain;
	}

	public void setClientChain(CertificateChainCertificate clientChain) {
		this.clientChain = clientChain;
	}

	public void setCodeSigningChain(CertificateChainCertificate codeSigningChain) {
		this.codeSigningChain = codeSigningChain;
	}

	public void setServerChain(CertificateChainCertificate serverChain) {
		this.serverChain = serverChain;
	}

	
	public CertificateChainCertificate getPersonsChain() {
		return personsChain;
	}

	
	public void setPersonsChain(CertificateChainCertificate personsChain) {
		this.personsChain = personsChain;
	}

}
