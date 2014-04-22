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

package be.fedict.eid.pkira.common.security;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.log.Log;
import org.opensaml.saml2.core.AuthnRequest;

import be.fedict.eid.idp.sp.protocol.saml2.AuthenticationRequestUtil;

/**
 * Base class for the authentication handler
 */
public abstract class AbstractAuthenticationHandlerBean implements AuthenticationHandler {

	@In(value = AuthenticationRequestDecoder.NAME, create = true)
	private AuthenticationRequestDecoder authenticationRequestDecoder;

	@In
	private EIdUserCredentials credentials;

	@In(value = "org.jboss.seam.international.localeSelector", create = true)
	private LocaleSelector localeSelector;

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
			String issuer = getIssuer(getRequest());

			HttpServletResponse response = getResponse();

			AuthnRequest authnRequest = AuthenticationRequestUtil.sendRequest(issuer, idpDestination, spDestination,
					null, null, response, localeSelector.getLanguage());
			String requestId = authnRequest.getID();
			// FIXME use Seam @Out
			getRequest().getSession().setAttribute(AuthenticationRequestDecoder.NAME_REQUEST_ID, requestId);

			FacesContext.getCurrentInstance().responseComplete();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}

	public static final String getIssuer(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		String scheme = request.getScheme();
		String hostName = request.getServerName();
		int port = request.getServerPort();

		url.append(scheme);
		url.append("://");
		url.append(hostName);
		if (port > 0
				&& ((scheme.equalsIgnoreCase("http") && port != 80) || (scheme.equalsIgnoreCase("https") && port != 443))) {
			url.append(':');
			url.append(port);
		}

		return url.toString();
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

	protected abstract String getSPReturnUrl();

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
		String returnURL = getSPReturnUrl();
		if (StringUtils.isBlank(returnURL)) {
			returnURL = getRequest().getRequestURL().toString();
		} else if (!returnURL.endsWith("/")) {
			returnURL += "/";
		}
		
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
