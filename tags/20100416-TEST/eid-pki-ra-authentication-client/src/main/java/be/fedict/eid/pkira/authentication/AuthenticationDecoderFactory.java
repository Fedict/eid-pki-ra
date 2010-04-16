/**
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

package be.fedict.eid.pkira.authentication;

/**
 * @author Bram Baeyens
 *
 */
public class AuthenticationDecoderFactory {
	
	private final AuthenticationRequestDecoder saml2RequestDecoder;
	private final AuthenticationRequestDecoder simpleRequestDecoder;
	
	public AuthenticationDecoderFactory() {
		this.saml2RequestDecoder = new Saml2RequestDecoder();
		this.simpleRequestDecoder = new SimpleRequestDecoder();
	}
	
	public AuthenticationRequestDecoder getAuthenticationRequestDecoder(
			AuthenticationType authenticationType) throws AuthenticationException {
		if (AuthenticationType.SIMPLE.equals(authenticationType)) {
			return simpleRequestDecoder;
		} else if (AuthenticationType.SAML2.equals(authenticationType)) {
			return saml2RequestDecoder;
		} else {
			throw new AuthenticationException("AuthenticationType " + authenticationType + " is not supported.");
		}
	}
}
