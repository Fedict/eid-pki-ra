package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.math.BigInteger;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.xkmsws.XKMSClient;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;

/**
 * XKMS Service implementation.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(XKMSService.NAME)
public class XKMSServiceBean implements XKMSService {

	@In(value = WebserviceLocator.NAME, create = true)
	private WebserviceLocator webserviceLocator;

	@In(value = CSRParser.NAME, create = true)
	private CSRParser csrParser;

	@In(value = CertificateParser.NAME, create = true)
	private CertificateParser certificateParser;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void revoke(CertificateRevocationContract contract) throws ContractHandlerBeanException {
		// Parse the certificate
		BigInteger serialNumber;
		try {
			serialNumber = certificateParser.parseCertificate(contract.getContractDocument()).getSerialNumber();
		} catch (CryptoException e) {
			// this should not occur: validation has to happen sooner
			throw new RuntimeException(e);
		}
		
		try {
			XKMSClient xkmsClient = webserviceLocator.getXKMSClient(contract.getCertificateDomain()
					.getCertificateAuthority());
			xkmsClient.revokeCertificate(serialNumber.toString());
		} catch (XKMSClientException e) {
			throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR, "Error revoking the certificate: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sign(CertificateSigningContract contract) throws ContractHandlerBeanException {
		// Convert the CSR to DER format
		byte[] csrData;
		try {
			csrData = csrParser.parseCSR(contract.getContractDocument()).getDerEncoded();
		} catch (CryptoException e) {
			// this should not occur: validation has to happen sooner
			throw new RuntimeException(e);
		}

		// Call XKMS
		byte[] certificateData;
		try {
			XKMSClient xkmsClient = webserviceLocator.getXKMSClient(contract.getCertificateDomain()
					.getCertificateAuthority());
			certificateData = xkmsClient.createCertificate(csrData, contract.getValidityPeriodMonths().intValue());
		} catch (XKMSClientException e) {
			throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR, "Error signing the certificate: "
					+ e.getMessage(), e);
		}
		
		// Convert the certificate to PEM format
		try {
			return certificateParser.parseCertificate(certificateData).getPemEncoded();
		} catch (CryptoException e) {
			throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR, "Invalid certificate retrieved from XKMS.", e);
		}
	}

	protected void setWebserviceLocator(WebserviceLocator webserviceLocator) {
		this.webserviceLocator = webserviceLocator;
	}

	protected void setCsrParser(CSRParser csrParser) {
		this.csrParser = csrParser;
	}

	protected void setCertificateParser(CertificateParser certificateParser) {
		this.certificateParser = certificateParser;
	}

}
