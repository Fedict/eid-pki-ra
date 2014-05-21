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

package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.math.BigInteger;
import java.text.MessageFormat;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.errorhandling.ApplicationComponent;
import be.fedict.eid.pkira.blm.errorhandling.ErrorLogger;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntry;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.blm.model.reporting.ReportManager;
import be.fedict.eid.pkira.crypto.certificate.CertificateParser;
import be.fedict.eid.pkira.crypto.csr.CSRParser;
import be.fedict.eid.pkira.crypto.exception.CryptoException;
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
	
	@In(value = ConfigurationEntryQuery.NAME, create=true)
	private ConfigurationEntryQuery configurationEntryQuery;

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
		byte[] csrData;
		try {
			// Convert the CSR to DER format
			csrData = csrParser.parseCSR(csr).getDerEncoded();

			// Create the client
			XKMSClient xkmsClient = webserviceLocator.getXKMSClient(contract.getCertificateDomain()
					.getCertificateAuthority());
			
			// Get timeshift
			ConfigurationEntry configurationEntry = configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.NOT_BEFORE_TIMESHIFT_SECOND);
			int timeShift = Integer.parseInt(configurationEntry.getValue());
			
			// Request the certificate
			byte[] certificateData;
			boolean success = false;
			try {
				certificateData = xkmsClient.createCertificate(csrData, contract.getValidityPeriodMonths().intValue(),
						contract.getCertificateType().toString(), timeShift);
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
	
	protected void setConfigurationEntryQuery(ConfigurationEntryQuery configurationEntryQuery) {
		this.configurationEntryQuery = configurationEntryQuery;
	}

}
