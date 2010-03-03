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
package be.fedict.eid.pkira.blm.model.eiddss;


import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Bean to verify the signature on a contract.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(SignatureVerifier.NAME)
public class SignatureVerifierBean implements SignatureVerifier {

	@Logger
	private Log log;
	
	private DigitalSignatureServiceClient dssClient;
	
	@PostConstruct
	public void initialize() {
		// Create the eid-dss client
		dssClient = new DigitalSignatureServiceClient();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.eiddss.SignatureVerification#verifySignature(
	 * java.lang.String)
	 */
	public String verifySignature(String requestMessage) throws ContractHandlerBeanException {
		try {
			String identity = dssClient.verifyWithSignerIdentity(requestMessage);
			if (identity != null) {
				return identity;
			}

			throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, "Signature is invalid.");
		} catch (RuntimeException e) {
			// eid-dss client throws runtime exception when something is wrong,
			// so let's handle this.
			log.error("Error during call to eid-dss to validate signature.", e);
			throw e;
		}
	}
	
	/**
	 * Injects the client.
	 */
	protected void setDigitalSignatureServiceClient(DigitalSignatureServiceClient dssClient) {
		this.dssClient = dssClient;
	}

	/**
	 * Injects the log.
	 */
	protected void setLog(Log log) {
		this.log= log;		
	}
}
