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

package be.fedict.eid.pkira.portal.security;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import be.fedict.eid.pkira.authentication.AuthenticationDecoderFactory;
import be.fedict.eid.pkira.authentication.AuthenticationException;
import be.fedict.eid.pkira.authentication.AuthenticationRequestDecoder;
import be.fedict.eid.pkira.authentication.AuthenticationType;
import be.fedict.eid.pkira.authentication.EIdUser;
import be.fedict.eid.pkira.generated.privatews.UserWS;
import be.fedict.eid.pkira.portal.domain.Operator;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * @author Bram Baeyens
 */
@Name(AuthenticationHandler.NAME)
public class AuthenticationHandlerBean implements AuthenticationHandler {

	@Logger
	private Log log;

	@Out(required = false, scope = ScopeType.SESSION)
	private Operator currentOperator;

	@In(value = "be.fedict.eid.pkira.auth.authenticationDecoderFactory")
	private AuthenticationDecoderFactory authenticationDecoderFactory;

	@In(create = true, value = EIDPKIRAPrivateServiceClient.NAME)
	private EIDPKIRAPrivateServiceClient eidPKIRAPrivateServiceClient;

	@In
	private Identity identity;

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
		enrichIdentity(eidUser);

		return true;
	}

	private void enrichIdentity(EIdUser eidUser) {
		// Create the user
		credentials.setUser(eidUser);

		// Mark as authenticated
		identity.addRole(PKIRARole.AUTHENTICATED_USER.name());

		// Create the operator
		currentOperator = new Operator();
		currentOperator.setName(eidUser.getFirstName() + " " + eidUser.getLastName());

		// Look up user in back-end and set roles if appropriate
		UserWS backendUser = eidPKIRAPrivateServiceClient.findUser(eidUser.getRRN());
		if (backendUser != null) {
			identity.addRole(PKIRARole.REGISTERED_USER.name());
		} else {
			identity.addRole(PKIRARole.UNREGISTERED_USER.name());
		}
	}

	private EIdUser determineLoggedInUser() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		try {
			AuthenticationRequestDecoder decoder = authenticationDecoderFactory
					.getAuthenticationRequestDecoder(AuthenticationType.SAML2);
			return decoder.decode(request);
		} catch (AuthenticationException e) {
			return null;
		}
	}
}
