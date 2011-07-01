package be.fedict.eid.pkira.blm.model.ca;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.crypto.CryptoException;

@Name(CertificateChainHandler.NAME)
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class CertificateChainHandler implements Serializable {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificateChainHandler";

	private static final long serialVersionUID = -5672530054643982245L;

	@In(value = CertificateAuthorityHome.NAME)
	private CertificateAuthorityHome certificateAuthorityHome;

	@In(value = CertificateChainHome.NAME, create = true)
	private CertificateChainHome certificateChainHome;

	@In(value = CertificateChainCertificateHome.NAME, create = true)
	private CertificateChainCertificateHome certificateChainCertificateHome;

	@In(value = CertificateParser.NAME, create = true)
	private CertificateParser certificateParser;

	@In
	private FacesMessages facesMessages;

	private final Map<CertificateChainCertificate, CertificateInfo> certificates = new HashMap<CertificateChainCertificate, CertificateInfo>();

	private byte[] certificateData;

	public void init() {
		// Get / create instance to use
		CertificateChain certificateChain = certificateAuthorityHome.getInstance().getCertificateChain();
		certificateChainHome.setInstance(certificateChain);
		if (certificateChain == null) {
			certificateChain = certificateChainHome.getInstance();
			certificateAuthorityHome.getInstance().setCertificateChain(certificateChain);

			certificateChainHome.persist();
			certificateAuthorityHome.update();
		}

		// Initialize certificates map
		try {
			for (CertificateChainCertificate certificate : certificateChain.getCertificates()) {
				CertificateInfo info = certificateParser.parseCertificate(certificate.getCertificateData());
				certificates.put(certificate, info);
			}
		} catch (CryptoException e) {
			facesMessages.addFromResourceBundle(Severity.FATAL, "certificatechain.upload.dberror");
			certificateChain = new CertificateChain();
			certificates.clear();
		}
	}

	public void setCertificateData(byte[] certificateData) {
		this.certificateData = certificateData;
	}

	public void uploadCertificate() {
		if (certificateData == null) {
			return;
		}

		// Parse the certificate
		CertificateInfo certificateInfo;
		try {
			certificateInfo = certificateParser.parseCertificate(certificateData);
		} catch (CryptoException e) {
			facesMessages.addFromResourceBundle(Severity.ERROR, "certificatechain.upload.invalidcertificate");
			return;
		}

		// Look for the issuer
		CertificateChainCertificate issuer = null;
		try {
			if (!certificateInfo.isSelfSigned()) {
				for (Map.Entry<CertificateChainCertificate, CertificateInfo> entry : certificates.entrySet()) {
					PublicKey publicKey = entry.getValue().getCertificate().getPublicKey();
					if (certificateInfo.isSignedBy(publicKey)) {
						issuer = entry.getKey();
					}
				}

				if (issuer == null) {
					facesMessages.addFromResourceBundle(Severity.ERROR, "certificatechain.upload.signerNotFound");
					return;
				}
			}
		} catch (CryptoException e) {
			facesMessages.addFromResourceBundle(Severity.ERROR, "certificatechain.upload.invalidcertificate");
			return;
		}

		// Create certificate object
		CertificateChainCertificate certificate = new CertificateChainCertificate();
		certificate.setCertificateChain(certificateChainHome.getInstance());
		certificate.setCertificateData(certificateInfo.getPemEncoded());
		certificate.setSerialNumber(certificateInfo.getSerialNumber().toString());
		certificate.setSubject(certificateInfo.getDistinguishedName());
		certificate.setIssuer(issuer);

		// Check if not already uploaded
		for (CertificateChainCertificate other : certificates.keySet()) {
			if (certificate.equals(other)) {
				facesMessages.addFromResourceBundle(Severity.ERROR, "certificatechain.upload.alreadyuploaded");
				return;
			}
		}
		certificates.put(certificate, certificateInfo);
		certificateChainHome.getInstance().getCertificates().add(certificate);
		certificateChainHome.update();
	}

	public void deleteCertificate(CertificateChainCertificate certificate) {
		if (certificate == null) {
			return;
		}

		// First calculate the certificates to delete
		List<CertificateChainCertificate> toDelete = new ArrayList<CertificateChainCertificate>();
		calculateCertificatesToDelete(certificate, toDelete);

		// Delete this one
		for (CertificateChainCertificate other : toDelete) {
			certificates.remove(other);
			certificateChainHome.getInstance().getCertificates().remove(other);

			certificateChainCertificateHome.setInstance(other);
			certificateChainCertificateHome.getInstance().setCertificateChain(null);
			certificateChainCertificateHome.update();
		}

		certificateChainHome.update();
	}

	private void calculateCertificatesToDelete(CertificateChainCertificate certificate,
			List<CertificateChainCertificate> toDelete) {
		// Add to list
		toDelete.add(0, certificate);

		// Unset if set on client, server or code signing chains
		CertificateChain certificateChain = certificateChainHome.getInstance();
		if (certificateChain.getClientChain() == certificate) {
			certificateChain.setClientChain(null);
			certificateChainHome.update();
		}
		if (certificateChain.getServerChain() == certificate) {
			certificateChain.setServerChain(null);
			certificateChainHome.update();
		}
		if (certificateChain.getCodeSigningChain() == certificate) {
			certificateChain.setCodeSigningChain(null);
			certificateChainHome.update();
		}
		if (certificateChain.getPersonsChain() == certificate) {
			certificateChain.setPersonsChain(null);
			certificateChainHome.update();
		}

		// Look for children
		for (CertificateChainCertificate other : certificateChain.getCertificates()) {
			if (ObjectUtils.equals(certificate, other.getIssuer())) {
				calculateCertificatesToDelete(other, toDelete);
			}
		}
	}

}
