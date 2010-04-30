/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see 
 * http://www.gnu.org/licenses/.
 */

package be.fedict.eid.pkira.xkmsws;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Element;

/**
 * Logging JAX-WS SOAP handler.
 * 
 * @author Frank Cornelis
 */
public class MessageInterceptionHandler implements SOAPHandler<SOAPMessageContext> {

	private Element lastOutboundMessage;
	private Element lastInboundMessage;

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext context) {
	}

	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	public boolean handleMessage(SOAPMessageContext context) {
		Element soapBody;
		try {
			soapBody = context.getMessage().getSOAPBody();
		} catch (SOAPException e) {
			throw new RuntimeException(e);
		}

		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty) {
			this.lastOutboundMessage = soapBody;
		} else {
			this.lastInboundMessage = soapBody;
		}

		return true;
	}

	public Element getLastOutboundMessage() {
		return lastOutboundMessage;
	}

	public Element getLastInboundMessage() {
		return lastInboundMessage;
	}
}
