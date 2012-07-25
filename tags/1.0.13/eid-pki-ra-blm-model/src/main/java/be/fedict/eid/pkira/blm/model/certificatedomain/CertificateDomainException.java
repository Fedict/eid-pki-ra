/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

package be.fedict.eid.pkira.blm.model.certificatedomain;

/**
 * @author Bram Baeyens
 *
 */
public abstract class CertificateDomainException extends Exception {

	private static final long serialVersionUID = -1076194728284213796L;
	
	private final String messageKey;
	
	public CertificateDomainException(String messageKey) {
		super();
		this.messageKey = messageKey;
	}
	
	public String getMessageKey() {
		return messageKey;
	}
}
