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

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.blm.model.contracthandler.services.ContractParser;
import be.fedict.eid.pkira.blm.model.contracthandler.services.FieldValidator;
import be.fedict.eid.pkira.blm.model.contracthandler.services.SignatureVerifier;
import be.fedict.eid.pkira.blm.model.contracthandler.services.XKMSService;
import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.domain.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.domain.CertificateType;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;
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
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
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

	@In(value = ContractParser.NAME, create = true)
	private ContractParser contractParser;

	@In(value = FieldValidator.NAME, create = true)
	private FieldValidator fieldValidator;

	@In(value = SignatureVerifier.NAME, create = true)
	private SignatureVerifier signatureVerifier;

	@In(value = XKMSService.NAME, create = true)
	private XKMSService xkmsService;

	@In(value = CertificateParser.NAME, create = true)
	private CertificateParser certificateParser;

	@In(value = DomainRepository.NAME, create = true)
	private DomainRepository repository;

	@In(value = MailTemplate.NAME, create = true)
	private MailTemplate mailTemplate;

	@Logger
	private Log log;

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractHandler#revokeCertificate(java.lang.String
	 * )
	 */
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

			// TODO: check authorization
			
			// Lookup the certificate
			Certificate certificate;
			try {
				CertificateInfo certificateInfo = certificateParser.parseCertificate(request.getCertificate());
				certificate = repository.findCertificate(certificateInfo.getIssuer(), certificateInfo
						.getSerialNumber());
				if (certificate==null) {
					throw new ContractHandlerBeanException(ResultType.UNKNOWN_CERTIFICATE, "The certificate was not found in our database.");
				}
			} catch (CryptoException e) {
				// this should not occur, as the certificate was already parsed
				// successfully before!
				throw new RuntimeException("Error while reparsing the certificate", e);
			}

			// Persist the contract
			CertificateRevocationContract contract = new CertificateRevocationContract();
			contract.setRequester(signer);
			contract.setContractDocument(requestMsg);
			contract.setSubject(request.getDistinguishedName());
			contract.setStartDate(request.getValidityStart().toGregorianCalendar().getTime());
			contract.setEndDate(request.getValidityEnd().toGregorianCalendar().getTime());
			repository.persistContract(contract);			

			// Call XKMS
			xkmsService.revoke(request.getCertificate());			

			// Delete the certificate
			repository.removeCertificate(certificate);
			
			// Return result message
			fillResponseFromRequest(responseBuilder, request, ResultType.SUCCESS, "Success");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(responseBuilder, request, e.getResultType(), e.getMessage());
		} catch (RuntimeException e) {
			log.error("Error while processing the contract", e);
			fillResponseFromRequest(responseBuilder, request, ResultType.GENERAL_FAILURE,
					"An error occurred while processing the contract.");
		}

		return contractParser.marshalResponseMessage(responseBuilder.toResponseType(),
				CertificateRevocationResponseType.class);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractHandler#signCertificate(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String signCertificate(String requestMsg) {
		CertificateSigningResponseBuilder responseBuilder = new CertificateSigningResponseBuilder();
		CertificateSigningRequestType request = null;
		try {
			// Parse the request
			request = contractParser.unmarshalRequestMessage(requestMsg, CertificateSigningRequestType.class);

			// Validate the fields
			fieldValidator.validateContract(request);

			// Validate the signature
			String signer = signatureVerifier.verifySignature(requestMsg);

			// TODO: check authorization

			// Persist the contract
			CertificateSigningContract contract = new CertificateSigningContract();
			contract.setRequester(signer);
			contract.setCertificateType(mapCertificateType(request.getCertificateType()));
			contract.setContractDocument(requestMsg);
			contract.setSubject(request.getDistinguishedName());
			contract.setValidityPeriodMonths(request.getValidityPeriodMonths().intValue());
			repository.persistContract(contract);

			// Call XKMS
			String certificateAsPem = xkmsService.sign(request.getCSR());
			if (certificateAsPem == null) {
				throw new ContractHandlerBeanException(ResultType.BACKEND_ERROR,
						"Error contacting the backend service.");
			}

			// Persist the certificate
			CertificateInfo certificateInfo;
			try {
				certificateInfo = certificateParser.parseCertificate(certificateAsPem);
			} catch (CryptoException e) {
				// TODO log problem with certificate received from CA
				throw new ContractHandlerBeanException(ResultType.GENERAL_FAILURE,
						"Error processing received certificate.");
			}
			Certificate certificate = new Certificate(certificateAsPem, certificateInfo, signer, contract);
			repository.persistCertificate(certificate);

			// Send the mail
			sendCertificateByMail(certificate);

			// All ok
			fillResponseFromRequest(responseBuilder, request, ResultType.SUCCESS, "Success");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(responseBuilder, request, e.getResultType(), e.getMessage());
		} catch (RuntimeException e) {
			log.error("Error while processing the contract", e);
			fillResponseFromRequest(responseBuilder, request, ResultType.GENERAL_FAILURE,
					"An error occurred while processing the contract.");
		}

		return contractParser.marshalResponseMessage(responseBuilder.toResponseType(),
				CertificateSigningResponseType.class);
	}

	private void sendCertificateByMail(Certificate certificate) {
		String templateName = "sendCertificateMail.ftl";
		Map<String, Object> parameters = Collections.singletonMap("certificate", (Object) certificate);
		String[] recipients = new String[]
			{ "j.vandenbergh@aca-it.be" };
		byte[] attachmentData = certificate.getX509().getBytes();
		String attachmentContentType = "application/x-pem-file";
		String attachmentFileName = "certificate.crt";

		mailTemplate.sendTemplatedMail(templateName, parameters, recipients, attachmentData, attachmentContentType,
				attachmentFileName);
	}

	private CertificateType mapCertificateType(CertificateTypeType certificateType) {
		switch (certificateType) {
		case CLIENT:
			return CertificateType.ClientCertificate;
		case SERVER:
			return CertificateType.ServerCertificate;
		case CODE:
			return CertificateType.CodeSigningCertificate;
		}
		throw new RuntimeException("Unmapped certificate type: " + certificateType);
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

	protected void setContractParser(ContractParser contractParser) {
		this.contractParser = contractParser;
	}

	protected void setFieldValidator(FieldValidator fieldValidator) {
		this.fieldValidator = fieldValidator;
	}

	protected void setSignatureVerifier(SignatureVerifier signatureVerifier) {
		this.signatureVerifier = signatureVerifier;
	}

	protected void setDomainRepository(DomainRepository domainRepository) {
		this.repository = domainRepository;
	}

	protected void setXkmsService(XKMSService xkmsService) {
		this.xkmsService = xkmsService;
	}

	protected void setCertificateParser(CertificateParser certificateParser) {
		this.certificateParser = certificateParser;
	}

	protected void setMailTemplate(MailTemplate mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	protected void setLog(Log log) {
		this.log = log;
	}
}
