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

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.security.Identity;

import be.fedict.eid.pkira.common.security.AbstractAuthenticationHandlerBean;
import be.fedict.eid.pkira.common.security.AuthenticationHandler;
import be.fedict.eid.pkira.common.security.EIdUser;
import be.fedict.eid.pkira.generated.privatews.UserWS;
import be.fedict.eid.pkira.portal.framework.Operator;
import be.fedict.eid.pkira.portal.util.ConfigurationEntryContainer;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * @author Bram Baeyens
 */
@Name(AuthenticationHandler.NAME)
public class AuthenticationHandlerBean extends AbstractAuthenticationHandlerBean {

	@Out(value = Operator.NAME, scope = ScopeType.SESSION, required = false)
	protected Operator currentOperator;

	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	protected EIDPKIRAPrivateServiceClient eidPKIRAPrivateServiceClient;
	
	@In(value = ConfigurationEntryContainer.NAME, create=true)
	protected ConfigurationEntryContainer configurationEntryContainer;
	
	@In(value = "org.jboss.seam.international.localeSelector", create = true)
	private LocaleSelector localeSelector;
	
	@In
	protected Identity identity;

	@Override
	public void enrichIdentity(EIdUser eidUser) {
		// Add the roles
		UserWS backendUser = eidPKIRAPrivateServiceClient.findUser(eidUser.getRRN());
		if (backendUser != null && backendUser.isWithRegistrations()) {
			identity.addRole(PKIRARole.REGISTERED_USER.name());
		} else {
			String localeString = localeSelector.getLocaleString();
			if (StringUtils.isEmpty(localeString)) {
				localeString = "en";
			}
			eidPKIRAPrivateServiceClient.createRegistrationForUser(eidUser.getRRN(), eidUser.getLastName(), eidUser.getFirstName(), null, null, localeString);
			identity.addRole(PKIRARole.UNREGISTERED_USER.name());
		}
		identity.addRole(PKIRARole.AUTHENTICATED_USER.name());

		// Create the operator
		currentOperator = new Operator();
		currentOperator.setName(eidUser.getFirstName() + " " + eidUser.getLastName());
	}

	@Override
	protected String getIDPDestination() {
		return configurationEntryContainer.getIDPDestination();
	}

	@Override
	protected String getSPReturnUrl() {
		return configurationEntryContainer.getSPReturnUrl();
	}
}
