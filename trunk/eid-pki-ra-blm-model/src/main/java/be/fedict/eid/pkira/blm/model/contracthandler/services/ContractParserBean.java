/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.RequestType;
import be.fedict.eid.pkira.generated.contracts.ResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * This class contains the code to marshal and unmarshal contract documents.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(ContractParser.NAME)
public class ContractParserBean implements ContractParser {

	@Logger
	private Log log;

	@In(value=EIDPKIRAContractsClient.NAME, create=true)
	private EIDPKIRAContractsClient contractsClient;
	
	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractParser#marshalResponseMessage(javax.xml
	 * .bind.JAXBElement)
	 */
	public <T extends ResponseType> String marshalResponseMessage(T response, Class<T> responseClazz) {
		try {
			return contractsClient.marshal(response, responseClazz);
		} catch (XmlMarshallingException e) {
			log.error("Error marshalling response back to client.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractParser#unmarshalRequestMessage(java.lang
	 * .String, java.lang.Class)
	 */
	public <T extends RequestType> T unmarshalRequestMessage(String requestMsg, Class<T> requestClass)
			throws ContractHandlerBeanException {
		try {
			return contractsClient.unmarshal(requestMsg, requestClass);
		} catch (XmlMarshallingException e) {
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, e.getMessage());
		}
	}

	/**
	 * Inject the log.
	 */
	protected void setLog(Log log) {
		this.log = log;
	}

	/**
	 * Inject the contracts client.
	 */
	protected void setContractsClient(EIDPKIRAContractsClient contractsClient) {
		this.contractsClient = contractsClient;
	}

}
