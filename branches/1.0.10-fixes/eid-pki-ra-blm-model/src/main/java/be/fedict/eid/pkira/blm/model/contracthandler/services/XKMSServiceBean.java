package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.math.BigInteger;
import java.text.MessageFormat;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.errorhandling.ApplicationComponent;
import be.fedict.eid.pkira.blm.errorhandling.ErrorLogger;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.blm.model.reporting.ReportManager;
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

	@In(value = ErrorLogger.NAME, create = true)
	private ErrorLogger errorLogger;

	@In(value = ReportManager.NAME, create = true)
	private ReportManager reportManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void revoke(AbstractContract contract, CertificateType certificateType, String certificateStr) throws ContractHandlerBeanException {
		// Parse the certificate
		BigInteger serialNumber;
		try {
			serialNumber = certificateParser.parseCertificate(certificateStr).getSerialNumber();

			XKMSClient xkmsClient = webserviceLocator.getXKMSClient(contract.getCertificateDomain()
					.getCertificateAuthority());

			boolean success = false;
			try {
				xkmsClient.revokeCertificate(serialNumber, certificateType.toString());
				success = true;
			} finally {
				reportManager.addLineToReport(contract, success);
			}
		} catch (XKMSClientException e) {
			logError(contract, e);
			throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR, "Error revoking the certificate: "
					+ e.getMessage(), e);
		} catch (Exception e) {
			logError(contract, e);
			throw new ContractHandlerBeanException(ResultType.GENERAL_FAILURE, "Error revoking the certificate: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sign(CertificateSigningContract contract, String csr) throws ContractHandlerBeanException {
		// Convert the CSR to DER format
		byte[] csrData;
		try {
			csrData = csrParser.parseCSR(csr).getDerEncoded();

			XKMSClient xkmsClient = webserviceLocator.getXKMSClient(contract.getCertificateDomain()
					.getCertificateAuthority());

			byte[] certificateData;
			boolean success = false;
			try {
				certificateData = xkmsClient.createCertificate(csrData, contract.getValidityPeriodMonths().intValue(),
						contract.getCertificateType().toString());
				success = true;
			} finally {
				reportManager.addLineToReport(contract, success);
			}

			return certificateParser.parseCertificate(certificateData).getPemEncoded();
		} catch (XKMSClientException e) {
			logError(contract, e);
			throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR, "Error signing the certificate: "
					+ e.getMessage(), e);
		} catch (CryptoException e) {
			logError(contract, e);
			throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR,
					"The created certificate could not be parsed.", e);
		} catch (Exception e) {
			logError(contract, e);
			throw new ContractHandlerBeanException(ResultType.GENERAL_FAILURE, "Error signing the certificate: "
					+ e.getMessage(), e);
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

	private void logError(AbstractContract contract, Exception e) {
		String message = MessageFormat.format("Error during XKMS invocation: {0}. Contract is: {1}.", e.getMessage(),
				contract);
		errorLogger.logError(ApplicationComponent.XKMS, message, e);
	}

	protected void setErrorLogger(ErrorLogger errorLogger) {
		this.errorLogger = errorLogger;
	}

	protected void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

}
