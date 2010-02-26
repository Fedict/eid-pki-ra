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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;

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
public class ContractParserBean implements ContractParser {

	private static Logger LOGGER = Logger.getLogger(ContractParserBean.class.getName());

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractParser#marshalResponseMessage(javax.xml
	 * .bind.JAXBElement)
	 */
	public <T extends ResponseType> String marshalResponseMessage(T response, Class<T> responseClazz) {
		try {
			return new EIDPKIRAContractsClient().marshalToBase64(response, responseClazz);
		} catch (XmlMarshallingException e) {
			LOGGER.log(Level.SEVERE, "Error marshalling response back to client.", e);
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
			return new EIDPKIRAContractsClient().unmarshalFromBase64(requestMsg, requestClass);
		} catch (XmlMarshallingException e) {
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, e.getMessage());
		}
	}

}
