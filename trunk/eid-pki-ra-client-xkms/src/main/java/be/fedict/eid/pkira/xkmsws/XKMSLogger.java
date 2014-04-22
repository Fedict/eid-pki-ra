/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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

/**
 * Interface used by the XKMS client to log things that happened.
 * 
 * @author jan
 */
public interface XKMSLogger {

	public enum XKMSMessageType {
		REQUEST, REVOCATION
	}

	String NAME = "be.fedict.eid.pkira.xkms.xkmsLogger";

	void logError(XKMSMessageType messageType, String requestMessage, byte[] responseMessage, Throwable t);

	void logSuccesfulInteraction(XKMSMessageType messageType, String requestMessage, byte[] responseMessage);

}
