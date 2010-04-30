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

package be.fedict.eid.pkira.common.security;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.authentication.AuthenticationDecoderFactory;
import be.fedict.eid.pkira.authentication.AuthenticationException;
import be.fedict.eid.pkira.authentication.AuthenticationRequestDecoder;
import be.fedict.eid.pkira.authentication.AuthenticationType;
import be.fedict.eid.pkira.authentication.EIdUser;

/**
 * Base class for the authentication handler
 */
public abstract class AbstractAuthenticationHandlerBean implements AuthenticationHandler {

	@Logger
	private Log log;

	@In(value = "be.fedict.eid.pkira.auth.authenticationDecoderFactory")
	private AuthenticationDecoderFactory authenticationDecoderFactory;

	@In
	private EIdUserCredentials credentials;

	public boolean authenticate() {
		log.info(">>> authenticate()");

		// Check for SAML2 authentication
		EIdUser eidUser = determineLoggedInUser();
		if (eidUser == null) {
			return false;
		}

		// Enrich the identity using this user object
		credentials.setUser(eidUser);
		enrichIdentity(eidUser);

		return true;
	}

	protected abstract void enrichIdentity(EIdUser eidUser);

	protected EIdUser determineLoggedInUser() {
		HttpServletRequest request = getRequest();
		try {
			AuthenticationRequestDecoder decoder = authenticationDecoderFactory
					.getAuthenticationRequestDecoder(AuthenticationType.SAML2);
			return decoder.decode(request);
		} catch (AuthenticationException e) {
			return null;
		}
	}

	protected HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request;
	}

	protected void setLog(Log log) {
		this.log = log;
	}

	protected void setAuthenticationDecoderFactory(AuthenticationDecoderFactory authenticationDecoderFactory) {
		this.authenticationDecoderFactory = authenticationDecoderFactory;
	}

	protected void setCredentials(EIdUserCredentials credentials) {
		this.credentials = credentials;
	}
}
