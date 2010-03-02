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
package be.fedict.eid.blm.model.contracthandler;

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import be.fedict.eid.blm.model.eiddss.SignatureVerification;
import be.fedict.eid.blm.model.validation.FieldValidator;
import be.fedict.eid.pkira.contracts.AbstractResponseBuilder;
import be.fedict.eid.pkira.contracts.CertificateRevocationResponseBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningResponseBuilder;
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
public class ContractHandlerBean implements ContractHandler {

	@EJB
	protected ContractParser contractParser;
	
	@EJB
	protected FieldValidator fieldValidator;
	
	@EJB
	protected SignatureVerification signatureVerification;
	
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
			String signer = signatureVerification.verifySignature(requestMsg);
			
			// TODO business logic
			// Return not implemented message
			fillResponseFromRequest(responseBuilder, request, ResultType.GENERAL_FAILURE, "Not implemented");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(responseBuilder, request, e.getResultType(), e.getMessage());
		}

		return contractParser.marshalResponseMessage(responseBuilder.toResponseType(), CertificateRevocationResponseType.class);
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
			String signer = signatureVerification.verifySignature(requestMsg);
			
			// TODO business logic
			// Return not implemented message
			fillResponseFromRequest(responseBuilder, request, ResultType.GENERAL_FAILURE, "Not implemented");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(responseBuilder, request, e.getResultType(), e.getMessage());
		}

		return contractParser.marshalResponseMessage(responseBuilder.toResponseType(), CertificateSigningResponseType.class);
	}

	/**
	 * Fills the values in the response, including the request id (when
	 * available).
	 */
	protected void fillResponseFromRequest(AbstractResponseBuilder<?> responseBuilder, RequestType request, ResultType resultType,
			String resultMessage) {
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
}
