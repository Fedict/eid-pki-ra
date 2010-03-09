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

import javax.ejb.Local;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.generated.contracts.RequestType;
import be.fedict.eid.pkira.generated.contracts.ResponseType;

/**
 * Interface of the bean used to marshal and unmarshal incoming Base64 XML
 * contracts.
 * 
 * @author Jan Van den Bergh
 */
@Local
public interface ContractParser {

	String NAME = "contractParser";

	/**
	 * Marshals a response message to a base64 encoded string. If this fails,
	 * null is returned and an error is logged with log level SEVERE.
	 * 
	 * @param response
	 *            the response message to marshal.
	 * @param responseClazz
	 *            the class of the response.>
	 * @return the Base64 encoded version of the message.
	 */
	public <T extends ResponseType> String marshalResponseMessage(T response, Class<T> responseClazz);

	/**
	 * Unmarshals the base64 encoding XML message.
	 * 
	 * @param requestMsg
	 *            the input string.
	 * @param requestClass
	 *            the expected message type.
	 * @return the parsed message.
	 * @throws ContractHandlerBeanException
	 *             if the message could not be parsed.
	 */
	<T extends RequestType> T unmarshalRequestMessage(String requestMsg, Class<T> requestClass)
			throws ContractHandlerBeanException;

}
