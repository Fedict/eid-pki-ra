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
package be.fedict.eid.pkira.client;

/**
 * Exception thrown when there are problems with the XML client.
 * 
 * @author jan
 */
public class PKIRAClientException extends Exception {

	private static final long serialVersionUID = 1218393649146025917L;

	public PKIRAClientException() {
	}

	public PKIRAClientException(String message) {
		super(message);
	}

	public PKIRAClientException(Throwable cause) {
		super(cause);
	}

	public PKIRAClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
