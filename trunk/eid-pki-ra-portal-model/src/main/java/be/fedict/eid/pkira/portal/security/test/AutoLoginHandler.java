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
package be.fedict.eid.pkira.portal.security.test;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.common.security.EIdUser;
import be.fedict.eid.pkira.generated.privatews.UserWS;
import be.fedict.eid.pkira.portal.security.AuthenticationHandlerBean;

/**
 * Handler to automatically login a test user when in debug mode and when a
 * specific user exists in the database.
 * 
 * @author Jan Van den Bergh
 */
@Name(AuthenticationHandlerBean.NAME)
@Install(debug = true, precedence = Install.DEPLOYMENT)
public class AutoLoginHandler extends AuthenticationHandlerBean {

	public static final String TEST_RRN = "99999999999";

	@Override
	protected EIdUser determineLoggedInUser() {
		EIdUser user = super.determineLoggedInUser();
		if (user!=null) {
			return user;
		}
		
		UserWS userWS = eidPKIRAPrivateServiceClient.findUser(TEST_RRN);
		if (userWS != null) {
			return new EIdUser(TEST_RRN, userWS.getFirstName(), userWS.getLastName());
		}
		
		return null;
	}

}
