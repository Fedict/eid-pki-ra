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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.core.Conversation;
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

	@Factory(value = NAME_LOGIN_URL, scope = ScopeType.EVENT)
	public String getLoginURL() {
		try {
			// TODO get from configuration
			String url = "https://www.e-contract.be/eid-idp-sp/saml-request?IdPDestination=https://www.e-contract.be/eid-idp/protocol/saml2";

			String returnURL = getRequest().getRequestURL().toString();
			returnURL = returnURL.replaceFirst("/[^/]*$", "/postLogin.seam");
			returnURL += "?cid=" + Conversation.instance().getId();
			String parameter = "SPDestination=" + URLEncoder.encode(returnURL, "UTF-8");

			if (url.indexOf('?') != -1) {
				return url + '&' + parameter;
			} else {
				return url + '?' + parameter;
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

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
