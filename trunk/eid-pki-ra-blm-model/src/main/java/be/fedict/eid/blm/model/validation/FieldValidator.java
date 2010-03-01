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
package be.fedict.eid.blm.model.validation;

import javax.ejb.Local;

import be.fedict.eid.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;

/**
 * Bean that validates the fields on a CertificateSigningRequest and
 * CertificateRevocationRequest.
 * 
 * @author Jan Van den Bergh
 */
@Local
public interface FieldValidator {

	/**
	 * Validates the fields on a certificate signing request.
	 * 
	 * @param contract
	 *            contract to validate.
	 * @throws ContractHandlerBeanException
	 *             if the contract is invalid. This exception describes what is
	 *             wrong.
	 */
	void validateContract(CertificateSigningRequestType contract) throws ContractHandlerBeanException;

	/**
	 * Validates the fields on a certificate revocation request.
	 * 
	 * @param contract
	 *            contract to validate.
	 * @throws ContractHandlerBeanException
	 *             if the contract is invalid. This exception describes what is
	 *             wrong.
	 */
	void validateContract(CertificateRevocationRequestType contract) throws ContractHandlerBeanException;
}
