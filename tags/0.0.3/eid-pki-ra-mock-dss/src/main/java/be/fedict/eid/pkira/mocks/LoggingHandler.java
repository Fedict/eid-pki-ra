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
package be.fedict.eid.pkira.mocks;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * @author Jan Van den Bergh
 */
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

	/*
	 * (non-Javadoc)
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext context) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext
	 * )
	 */
	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		logToSystemOut(smc);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @seejavax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.
	 * MessageContext)
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		logToSystemOut(smc);
		return true;
	}

	private void logToSystemOut(SOAPMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty.booleanValue()) {
			System.out.println("\nOutbound message:");
		} else {
			System.out.println("\nInbound message:");
		}

		SOAPMessage message = smc.getMessage();
		try {
			message.writeTo(System.out);
			System.out.println("");
		} catch (Exception e) {
			System.out.println("Exception in handler: " + e);
		}
	}
}
