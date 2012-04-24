/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.mappers;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;
import be.fedict.eid.pkira.generated.privatews.UserWS;

/**
 * Implementation of the user mapper.
 * 
 * @author Jan Van den Bergh
 */
@Name(UserMapper.NAME)
@Scope(ScopeType.STATELESS)
public class UserMapperBean implements UserMapper {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserWS map(User user, boolean hasApprovedRegistrations) {
		if (user == null) {
			return null;
		}

		UserWS result = new ObjectFactory().createUserWS();
		result.setId(user.getId().toString());
		result.setFirstName(user.getFirstName());
		result.setLastName(user.getLastName());
		result.setNationalRegisterNumber(user.getNationalRegisterNumber());
		result.setWithRegistrations(hasApprovedRegistrations);
		result.setLocale(user.getLocale());

		return result;
	}

}
