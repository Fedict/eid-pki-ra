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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import be.fedict.eid.idp.sp.protocol.saml2.AuthenticationRequestUtil;

/**
 * Base class for the authentication handler
 */
public abstract class AbstractAuthenticationHandlerBean implements AuthenticationHandler {

	@In(value = AuthenticationRequestDecoder.NAME, create = true)
	private AuthenticationRequestDecoder authenticationRequestDecoder;

	@In
	private EIdUserCredentials credentials;

	@Logger
	private Log log;

	@Override
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

	@Override
	public void sendAuthenticationRequest() {
		try {
			String idpDestination = getIDPDestination();
			String spDestination = getSPDestination();

			HttpServletResponse response = getResponse();

			AuthenticationRequestUtil.sendRequest(idpDestination, spDestination, null, null, response);
			FacesContext.getCurrentInstance().responseComplete();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}

	protected EIdUser determineLoggedInUser() {
		HttpServletRequest request = getRequest();
		try {
			return authenticationRequestDecoder.decode(request);
		} catch (AuthenticationException e) {
			return null;
		}
	}

	protected abstract void enrichIdentity(EIdUser eidUser);

	protected abstract String getIDPDestination();

	protected HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request;
	}

	protected HttpServletResponse getResponse() {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		return response;
	}

	protected String getSPDestination() {
		String returnURL = getRequest().getRequestURL().toString();
		returnURL = returnURL.replaceFirst("/[^/]*$", "/postLogin.seam");
		return returnURL;
	}

	protected void setAuthenticationRequestDecoder(AuthenticationRequestDecoder authenticationRequestDecoder) {
		this.authenticationRequestDecoder = authenticationRequestDecoder;
	}

	protected void setCredentials(EIdUserCredentials credentials) {
		this.credentials = credentials;
	}

	protected void setLog(Log log) {
		this.log = log;
	}
}
