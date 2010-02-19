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
package be.fedict.eid.blm.model;

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import be.fedict.eid.pkira.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.contracts.ObjectFactory;
import be.fedict.eid.pkira.contracts.RequestType;
import be.fedict.eid.pkira.contracts.ResponseType;
import be.fedict.eid.pkira.contracts.ResultType;

/**
 * Contract handler bean which is used to handle incoming contracts and send a
 * response back.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
public class ContractHandlerBean implements ContractHandler {

	/**
	 * Object factory for JAXB objects.
	 */
	private static final ObjectFactory FACTORY = new ObjectFactory(); 

	@EJB
	protected ContractParser contractParser;
	
	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractHandler#revokeCertificate(java.lang.String
	 * )
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String revokeCertificate(String requestMsg) {		
		CertificateRevocationResponseType response = FACTORY.createCertificateRevocationResponseType();
		CertificateRevocationRequestType request = null;
		try {
			// Parse the request
			request = contractParser.unmarshalRequestMessage(requestMsg, CertificateRevocationRequestType.class);

			// TODO business logic
			// Return not implemented message
			fillResponseFromRequest(response, request, ResultType.GENERAL_FAILURE, "Not implemented");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(response, request, e.getResultType(), e.getMessage());
		}

		return contractParser.marshalResponseMessage(FACTORY.createCertificateRevocationResponse(response));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractHandler#signCertificate(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String signCertificate(String requestMsg) {		
		CertificateSigningResponseType response = FACTORY.createCertificateSigningResponseType();
		CertificateSigningRequestType request = null;
		try {
			// Parse the request
			request = contractParser.unmarshalRequestMessage(requestMsg, CertificateSigningRequestType.class);

			// TODO business logic
			// Return not implemented message
			fillResponseFromRequest(response, request, ResultType.GENERAL_FAILURE, "Not implemented");
		} catch (ContractHandlerBeanException e) {
			fillResponseFromRequest(response, request, e.getResultType(), e.getMessage());
		}

		return contractParser.marshalResponseMessage(FACTORY.createCertificateSigningResponse(response));
	}

	/**
	 * Fills the values in the response, including the request id (when
	 * available).
	 */
	protected void fillResponseFromRequest(ResponseType response, RequestType request, ResultType resultType,
			String resultMessage) {
		if (request != null) {
			response.setRequestId(request.getRequestId());
		}
		response.setResponseId(generateResponseId());
		response.setResult(resultType);
		response.setResultMessage(resultMessage);
	}

	/**
	 * Generates a unique response ID.
	 */
	protected String generateResponseId() {
		return UUID.randomUUID().toString();
	}
}
