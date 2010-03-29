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

package be.fedict.eid.pkira.blm.model.security;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Identity;

import be.fedict.eid.pkira.authentication.EIdUser;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserRepository;
import be.fedict.eid.pkira.common.security.AbstractAuthenticationHandlerBean;
import be.fedict.eid.pkira.common.security.AuthenticationHandler;

/**
 * @author Bram Baeyens
 */
@Name(AuthenticationHandler.NAME)
public class AuthenticationHandlerBean extends AbstractAuthenticationHandlerBean {

	@In(value = UserRepository.NAME, create=true)
	private UserRepository userRepository;
	
	@In
	private Identity identity;

	@Override
	public void enrichIdentity(EIdUser eidUser) {		
		// Mark as authenticated
		identity.addRole(PKIRARole.AUTHENTICATED_USER.name());

		// Look up the user object or create it
		User user = userRepository.findByNationalRegisterNumber(eidUser.getRRN());
		if (user == null) {
			user = new User();
			user.setFirstName(eidUser.getFirstName());
			user.setLastName(eidUser.getLastName());
			user.setNationalRegisterNumber(eidUser.getRRN());
			userRepository.persist(user);
		}

		// Determine the roles
		if (user.isAdmin()) {
			identity.addRole(PKIRARole.ADMIN_USER.name());
		}
	}
}
