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

package be.fedict.eid.pkira.blm.model.usermgmt;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.framework.ValidatingEntityHome;
import be.fedict.eid.pkira.common.security.EIdUserCredentials;

/**
 * @author Bram Baeyens
 *
 */
@Name(UserHome.NAME)
@Scope(ScopeType.CONVERSATION)
public class UserHome extends ValidatingEntityHome<User> {

	private static final long serialVersionUID = -9041171409557145771L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.userHome";
	
	@In(required = true)
	private EIdUserCredentials credentials;
	
	@Override
	protected String getUpdatedMessageKey() {
		return "user.updated";
	}
	
	public boolean isCurrentUser() {
		return getInstance().getNationalRegisterNumber().equals(credentials.getUser().getRRN());
	}
	
	public String revokeAdmin() {
		getInstance().setAdmin(false);
		return update();
	}
	
	public String grantAdmin() {
		getInstance().setAdmin(true);
		return update();
	}

}
