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
package be.fedict.eid.pkira.ws.impl;

import javax.ejb.EJB;
import javax.jws.WebService;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandler;
import be.fedict.eid.pkira.generated.publicws.EIDPKIRAPortType;

/**
 * Implementation of the public web service. This class delegates all
 * invocations to a contract handler EJB.
 * 
 * @author Jan Van den Bergh
 */
@WebService(endpointInterface = "be.fedict.eid.pkira.generated.publicws.EIDPKIRAPortType")
public class EIDPKIRAServiceImpl implements EIDPKIRAPortType {

	@EJB
	private ContractHandler contractHandler;

	/**
	 * Revoke certificate implementation.
	 */
	@Override
	public String revokeCertificate(String request) {
		return contractHandler.revokeCertificate(request);
	}

	/**
	 * Sign certificate implementation.
	 */
	@Override
	public String signCertificate(String request) {
		return contractHandler.signCertificate(request);
	}

}
