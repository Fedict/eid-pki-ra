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

package be.fedict.eid.pkira.portal.handler;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

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
 *
 */
@Name(AuthenticationHandler.NAME)
public class AuthenticationHandlerBean implements AuthenticationHandler {

	@Logger
	private Log log;
	
	@Out(required=false, scope=ScopeType.SESSION) 
	private Operator currentOperator;
	@In(value="be.fedict.eid.pkira.auth.authenticationDecoderFactory")
	private AuthenticationDecoderFactory authenticationDecoderFactory;
	@In(create=true, value="eidPKIRAPrivateServiceClient") 
	private EIDPKIRAPrivateServiceClient eidPKIRAPrivateServiceClient;
	@In FacesMessages facesMessages;
	
	public boolean authenticate() {
		log.info(">>> authenticate()");
		HttpServletRequest request = 
			(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		AuthenticationType authenticationType = Enum.valueOf(AuthenticationType.class, request.getParameter("authenticationType"));
		try {
			AuthenticationRequestDecoder decoder = authenticationDecoderFactory.getAuthenticationRequestDecoder(authenticationType);	
			EIdUser eidUser = decoder.decode(request);
			UserWS user = validateUser(eidUser);	
			currentOperator = new Operator();
			currentOperator.setName(user.getFirstName().concat(" ").concat(user.getLastName()));
		} catch (AuthenticationException e) {
			facesMessages.addFromResourceBundle("validator.invalid.authentication");
			log.info("<<< authenticate: failure");
			return false;
		}
		log.info("<<< authenticate: success");
		return true;
	}

	private UserWS validateUser(EIdUser eidUser) throws AuthenticationException {
		UserWS user = eidPKIRAPrivateServiceClient.findUser(eidUser.getIdentifier());	
		if (user == null) {
			throw new AuthenticationException("Unknown user");
		}
		return user;
	}
}
