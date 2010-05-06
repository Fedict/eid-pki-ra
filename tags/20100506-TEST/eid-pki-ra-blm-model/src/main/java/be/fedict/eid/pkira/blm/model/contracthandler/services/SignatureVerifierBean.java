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
package be.fedict.eid.pkira.blm.model.contracthandler.services;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Bean to verify the signature on a contract.
 * 
 * @author Jan Van den Bergh
 */
@Name(SignatureVerifier.NAME)
@Scope(ScopeType.STATELESS)
public class SignatureVerifierBean implements SignatureVerifier {

	@Logger
	private Log log;

	@In(value = WebserviceLocator.NAME, create = true)
	private WebserviceLocator webserviceLocator;

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.eiddss.SignatureVerification#verifySignature(
	 * java.lang.String)
	 */
	public String verifySignature(String requestMessage) throws ContractHandlerBeanException {
		DigitalSignatureServiceClient dssClient = webserviceLocator.getDigitalSignatureServiceClient();
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
			throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, "Error verifying signature", e);
		}
	}

	/**
	 * Injects the log.
	 */
	protected void setLog(Log log) {
		this.log = log;
	}

	protected void setWebserviceLocator(WebserviceLocator webserviceLocator) {
		this.webserviceLocator = webserviceLocator;
	}
}
