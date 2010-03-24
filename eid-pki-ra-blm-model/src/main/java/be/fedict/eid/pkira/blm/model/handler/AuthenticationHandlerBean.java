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

package be.fedict.eid.pkira.blm.model.handler;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.authentication.AuthenticationDecoderFactory;
import be.fedict.eid.pkira.authentication.AuthenticationException;
import be.fedict.eid.pkira.authentication.AuthenticationType;
import be.fedict.eid.pkira.authentication.EIdUser;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserRepository;

/**
 * @author Bram Baeyens
 *
 */
@Name(AuthenticationHandler.NAME)
public class AuthenticationHandlerBean implements AuthenticationHandler {

	@Logger private Log log;
	
	@In(create=true, value=UserRepository.NAME)
	private UserRepository userRepository;
	@Out(required=false, scope=ScopeType.SESSION) 
	private User currentUser;
	@In(value="authenticationDecoderFactory")
	private AuthenticationDecoderFactory authenticationDecoderFactory;
	
	@Override
	public boolean authenticate() {
		log.info(">>> authenticate()");
		try {
			EIdUser eidUser = getUserCredentials();
			if (eidUser == null) {
				log.info("<<< authenticate: failure, no user on request");
				return false;
			}
			currentUser = userRepository.findByNationalRegisterNumber(eidUser.getIdentifier());
			if (currentUser == null) {
				log.info("<<< authenticate: failure, user doesn't exist");
				return false;
			}
		} catch (AuthenticationException e) {
			log.info("<<< authenticate: failure");
			return false;
		}
		log.info("<<< authenticate: success");
		return true;
	}
	
	@Override
	public EIdUser getUserCredentials() throws AuthenticationException {
		log.debug(">>> getUserCredentials()");
		HttpServletRequest request = 
			(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		AuthenticationType authenticationType = Enum.valueOf(AuthenticationType.class, request.getParameter("authenticationType"));
		EIdUser eiIdUser = authenticationDecoderFactory.getAuthenticationRequestDecoder(authenticationType).decode(request);
		log.debug("<<< getUserCredentials: {0}", eiIdUser);
		return eiIdUser;
	}
}
