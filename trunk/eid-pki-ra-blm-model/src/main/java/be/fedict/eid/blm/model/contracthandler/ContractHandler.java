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

import javax.ejb.Local;

/**
 * Interface of the contract handler bean which is used to handle incoming
 * contracts and send a response back.
 * 
 * @author Jan Van den Bergh
 */
@Local
public interface ContractHandler {

	/**
	 * Handles a certificate signing request.
	 * 
	 * @param request
	 *            request to sign: XML document, base-64 encoded.
	 * @return response message: XML document, base-64 encoded.
	 */
	String signCertificate(String request);

	/**
	 * Handles a certificate revocation request.
	 * 
	 * @param request
	 *            request to sign: XML document, base-64 encoded.
	 * @return response message: XML document, base-64 encoded.
	 */
	 String revokeCertificate(String request);

}