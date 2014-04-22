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

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Credentials;

import static org.jboss.seam.ScopeType.SESSION;


/**
 * Credentials used by our application.
 * 
 * @author Jan Van den Bergh
 */
@Name("org.jboss.seam.security.credentials")
@Scope(SESSION)
public class EIdUserCredentials extends Credentials {

	private static final long serialVersionUID = -5978049996605832344L;

	private EIdUser user;

	public EIdUser getUser() {
		return user;
	}

	public void setUser(EIdUser user) {
		this.user = user;
		setInitialized(user != null);
	}

	@Override
	public String getUsername() {
		return user != null ? user.getRRN() : null;
	}
	
	public String getFullName() {
		return user != null ? user.getFirstName() + " " + user.getLastName() : null;
	}

	@Override
	@Deprecated
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	@Deprecated
	public void setPassword(String password) {
		super.setPassword(password);
	}

	@Override
	@Deprecated
	public void setUsername(String username) {
		super.setUsername(username);
	}
}
