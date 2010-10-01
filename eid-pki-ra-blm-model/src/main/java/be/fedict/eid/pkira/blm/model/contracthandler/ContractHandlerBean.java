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
package be.fedict.eid.pkira.blm.model.contracthandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.log.Log;
import org.quartz.SchedulerException;

import be.fedict.eid.pkira.blm.model.ca.CertificateChainCertificate;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.blm.model.contracthandler.services.ContractParser;
import be.fedict.eid.pkira.blm.model.contracthandler.services.FieldValidator;
import be.fedict.eid.pkira.blm.model.contracthandler.services.SchedulerBean;
import be.fedict.eid.pkira.blm.model.contracthandler.services.SignatureVerifier;
import be.fedict.eid.pkira.blm.model.contracthandler.services.XKMSService;
import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.contracts.ContractRepository;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationManager;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.contracts.AbstractResponseBuilder;
import be.fedict.eid.pkira.contracts.CertificateRevocationResponseBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningResponseBuilder;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.RequestType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Contract handler bean which is used to handle incoming contracts and send a
 * response back.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(ContractHandler.NAME)
public class ContractHandlerBean implements ContractHandler {

	@In(value = CertificateParser.NAME, create = true)
	private CertificateParser certificateParser;

	@In(value = ContractParser.NAME, create = true)
	private ContractParser contractParser;

	@In(value = ContractRepository.NAME, create = true)
	private ContractRepository contractRepository;

	@In(value = FieldValidator.NAME, create = true)
	private FieldValidator fieldValidator;

	@In(value = SchedulerBean.NAME, create = true)
	private SchedulerBean schedulerBean;

	@Logger
	private Log log;

	@In(value = MailTemplate.NAME, create = true)
	private MailTemplate mailTemplate;

	@In(value = RegistrationManager.NAME, create = true)
	private RegistrationManager registrationManager;

	@In(value = SignatureVerifier.NAME, create = true)
	private SignatureVerifier signatureVerifier;

	@In(value = XKMSService.NAME, create = true)
	private XKMSService xkmsService;;

	@In(value = ConfigurationEntryQuery.NAME, create = true)
	private ConfigurationEntryQuery configurationEntryQuery;

	/** {@inheritDoc} */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String revokeCertificate(String requestMsg) {
		CertificateRevocationResponseBuilder responseBuilder = new CertificateRevocationResponseBuilder();
		CertificateRevocationRequestType request = null;
		try {
			// Parse the request
			request = contractParser.unmarshalRequestMessage(requestMsg, CertificateRevocationRequestType.class);

			// Validate the fields
			fieldValidator.validateContract(request);

			// Validate the signature
			String signer = signatureVerifier.verifySignature(requestMsg);

			// Lookup the certificate
			Certificate certificate = findCertificate(request);

			// Check the authorization
			Registration registration = getMatchingRegistration(signer, request.getDistinguishedName(),
					certificate.getCertificateType());

			// Check the legal notice
			checkLegalNotice(request, registration);

			// Persist the contract
			AbstractContract contract = saveContract(registration, requestMsg, request, signer);

			// Call XKMS
			xkmsService.revoke(contract, certificate.getCertificateType());

			// Delete the certificate
			contractRepository.removeCertificate(certificate);

			certificate.cancelNotificationMail();

			// Return result message
			fillResponseFromRequest(responseBuilder, request, ResultType.SUCCESS, "Success");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(responseBuilder, request, e.getResultType(), e.getMessage());
		} catch (RuntimeException e) {
			log.error("Error while processing the contract", e);
			fillResponseFromRequest(responseBuilder, request, ResultType.GENERAL_FAILURE,
					"An error occurred while processing the contract.");
		} catch (SchedulerException e) {
			log.error("Error while scheduling the notification mail.", e);
		}

		return contractParser.marshalResponseMessage(responseBuilder.toResponseType(),
				CertificateRevocationResponseType.class);
	}

	/** {@inheritDoc} */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String signCertificate(String requestMsg) {
		CertificateSigningResponseBuilder responseBuilder = new CertificateSigningResponseBuilder();
		CertificateSigningRequestType request = null;
		int certificateId = -1;
		try {
			// Parse the request
			request = contractParser.unmarshalRequestMessage(requestMsg, CertificateSigningRequestType.class);

			// Validate the fields
			fieldValidator.validateContract(request);

			// Validate the signature
			String signer = signatureVerifier.verifySignature(requestMsg);

			// Check if the user is authorized
			CertificateType certificateType = mapCertificateType(request);
			Registration registration = getMatchingRegistration(signer, request.getDistinguishedName(), certificateType);

			// Check the legal notice
			checkLegalNotice(request, registration);

			// Persist the contract
			CertificateSigningContract contract = saveContract(registration, requestMsg, request, signer,
					certificateType);

			// Call XKMS
			String certificateAsPem = xkmsService.sign(contract);
			if (certificateAsPem == null) {
				throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR,
						"Error contacting the backend service.");
			}

			// Persist the certificate
			CertificateInfo certificateInfo;
			try {
				certificateInfo = certificateParser.parseCertificate(certificateAsPem);
			} catch (CryptoException e) {
				throw new ContractHandlerBeanException(ResultType.GENERAL_FAILURE,
						"Error processing received certificate.");
			}

			User requester = registration.getRequester();
			String requesterName = requester.getName();
			Certificate certificate = new Certificate(certificateAsPem, certificateInfo, requesterName, contract);
			scheduleNotificationMail(registration, certificateInfo, certificate);
			contractRepository.persistCertificate(certificate);

			// Send the mail
			sendCertificateByMail(certificate, registration);

			// All ok
			certificateId = certificate.getId();
			fillResponseFromRequest(responseBuilder, request, ResultType.SUCCESS, "Success");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(responseBuilder, request, e.getResultType(), e.getMessage());
		} catch (RuntimeException e) {
			log.error("Error while processing the contract", e);
			fillResponseFromRequest(responseBuilder, request, ResultType.GENERAL_FAILURE,
					"An error occurred while processing the contract.");
		}

		CertificateSigningResponseType responseType = responseBuilder.toResponseType(certificateId);
		return contractParser.marshalResponseMessage(responseType, CertificateSigningResponseType.class);
	}

	private void checkLegalNotice(RequestType request, Registration registration) throws ContractHandlerBeanException {
		String incomingLegalNotice = StringUtils.trimToEmpty(request.getLegalNotice());
		String expectedLegalNotice = StringUtils.trimToEmpty(registration.getCertificateDomain()
				.getCertificateAuthority().getLegalNotice());

		if (!StringUtils.equals(expectedLegalNotice, incomingLegalNotice)) {
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, "Invalid legal notice");
		}
	}

	protected void scheduleNotificationMail(Registration registration, CertificateInfo certificateInfo,
			Certificate certificate) {
		Long intervalParam = Long.valueOf(configurationEntryQuery.findByEntryKey(
				ConfigurationEntryKey.NOTIFICATION_MAIL_MINUTES).getValue());

		Date when;
		if (intervalParam > 0) {
			when = certificateInfo.getValidityEnd();
			when.setTime(when.getTime() - intervalParam * 1000 * 60);
		} else {
			when = new Date();
			when.setTime(when.getTime() - intervalParam * 1000 * 60);
		}
		QuartzTriggerHandle timer = schedulerBean.scheduleNotifcation(when, certificate.getIssuer(),
				certificate.getSerialNumber());
		certificate.setTimer(timer);
	}

	private Certificate findCertificate(CertificateRevocationRequestType request) throws ContractHandlerBeanException {
		Certificate certificate;
		try {
			CertificateInfo certificateInfo = certificateParser.parseCertificate(request.getCertificate());
			certificate = contractRepository.findCertificate(certificateInfo.getIssuer(),
					certificateInfo.getSerialNumber());
			if (certificate == null) {
				throw new ContractHandlerBeanException(ResultType.UNKNOWN_CERTIFICATE,
						"The certificate was not found in our database.");
			}
		} catch (CryptoException e) {
			// this should not occur, as the certificate was already parsed
			// successfully before!
			throw new RuntimeException("Error while reparsing the certificate", e);
		}
		return certificate;
	}

	private Registration getMatchingRegistration(String signer, String distinguishedName, CertificateType type)
			throws ContractHandlerBeanException {
		Registration registration = registrationManager.findRegistrationForUserDNAndCertificateType(signer,
				distinguishedName, type);
		if (registration == null) {
			throw new ContractHandlerBeanException(ResultType.NOT_AUTHORIZED,
					"User is not authorized for the DN in the contract");
		}
		return registration;
	}

	private CertificateType mapCertificateType(CertificateSigningRequestType request) {
		CertificateType certificateType = Enum.valueOf(CertificateType.class, request.getCertificateType().name());
		return certificateType;
	}

	private AbstractContract saveContract(Registration registration, String requestMsg,
			CertificateRevocationRequestType request, String signer) {
		CertificateRevocationContract contract = new CertificateRevocationContract();
		contract.setRequester(signer);
		contract.setCreationDate(new Date());
		contract.setContractDocument(requestMsg);
		contract.setSubject(request.getDistinguishedName());
		contract.setStartDate(request.getValidityStart().toGregorianCalendar().getTime());
		contract.setEndDate(request.getValidityEnd().toGregorianCalendar().getTime());
		contract.setCertificateDomain(registration.getCertificateDomain());
		contractRepository.persistContract(contract);

		return contract;
	}

	private CertificateSigningContract saveContract(Registration registration, String requestMsg,
			CertificateSigningRequestType request, String signer, CertificateType certificateType) {
		CertificateSigningContract contract = new CertificateSigningContract();
		contract.setRequester(signer);
		contract.setCreationDate(new Date());
		contract.setCertificateType(certificateType);
		contract.setContractDocument(requestMsg);
		contract.setSubject(request.getDistinguishedName());
		contract.setValidityPeriodMonths(request.getValidityPeriodMonths().intValue());
		contract.setCertificateDomain(registration.getCertificateDomain());
		contractRepository.persistContract(contract);
		return contract;
	}

	private void sendCertificateByMail(Certificate certificate, Registration registration)
			throws ContractHandlerBeanException {
		String templateName = "sendCertificateMail.ftl";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("certificate", certificate);
		parameters.put("user", registration.getRequester());

		byte[] attachmentData = null;
		ZipOutputStream zip = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			zip = new ZipOutputStream(baos);

			zip.putNextEntry(new ZipEntry("certificate.crt"));

			zip.write(certificate.getX509().getBytes());
			zip.closeEntry();

			int i = 0;
			CertificateChainCertificate chain = certificate.getCertificateChainCertificate();
			while (chain != null) {
				zip.putNextEntry(new ZipEntry("chain" + (i++) + ".crt"));
				zip.write(chain.getCertificateData());
				zip.closeEntry();
				chain = chain.getIssuer();
			}

			zip.finish();
			zip.close();

			baos.close();

			String[] recipients = new String[]
				{ registration.getEmail() };
			attachmentData = baos.toByteArray();
			String attachmentContentType = "application/zip";
			String attachmentFileName = certificate.getSerialNumber() + ".zip";

			mailTemplate.sendTemplatedMail(templateName, parameters, recipients, attachmentData, attachmentContentType,
					attachmentFileName);

		} catch (IOException e) {
			log.error("Error creating zip file for certificate chain", e);
			throw new ContractHandlerBeanException(ResultType.GENERAL_FAILURE,
					"Failed to create zip file for certificate chain.");
		}
	}

	/**
	 * Fills the values in the response, including the request id (when
	 * available).
	 */
	protected void fillResponseFromRequest(AbstractResponseBuilder<?> responseBuilder, RequestType request,
			ResultType resultType, String resultMessage) {
		if (request != null) {
			responseBuilder.setRequestId(request.getRequestId());
		}
		responseBuilder.setResult(resultType);
		responseBuilder.setResultMessage(resultMessage);
	}

	/**
	 * Generates a unique response ID.
	 */
	protected String generateResponseId() {
		return UUID.randomUUID().toString();
	}

	protected void setCertificateParser(CertificateParser certificateParser) {
		this.certificateParser = certificateParser;
	}

	protected void setContractParser(ContractParser contractParser) {
		this.contractParser = contractParser;
	}

	protected void setContractRepository(ContractRepository contractRepository) {
		this.contractRepository = contractRepository;
	}

	protected void setFieldValidator(FieldValidator fieldValidator) {
		this.fieldValidator = fieldValidator;
	}

	protected void setLog(Log log) {
		this.log = log;
	}

	protected void setMailTemplate(MailTemplate mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	protected void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	protected void setSignatureVerifier(SignatureVerifier signatureVerifier) {
		this.signatureVerifier = signatureVerifier;
	}

	protected void setXkmsService(XKMSService xkmsService) {
		this.xkmsService = xkmsService;
	}

	protected void setConfigurationEntryQuery(ConfigurationEntryQuery configurationEntryQuery) {
		this.configurationEntryQuery = configurationEntryQuery;
	}
}
