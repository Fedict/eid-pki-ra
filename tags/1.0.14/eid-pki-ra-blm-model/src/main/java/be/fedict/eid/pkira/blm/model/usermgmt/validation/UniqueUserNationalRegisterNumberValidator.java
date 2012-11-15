/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

package be.fedict.eid.pkira.blm.model.usermgmt.validation;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Validator;
import org.jboss.seam.Component;

import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserHome;

/**
 * @author Bram Baeyens
 */
public class UniqueUserNationalRegisterNumberValidator implements Validator<UniqueUserNationalRegisterNumber> {

	@Override
	public void initialize(UniqueUserNationalRegisterNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value) {
		if (StringUtils.isEmpty((String) value)) {
			return true;
		}
		UserHome userHome = (UserHome) Component.getInstance(UserHome.NAME);
		User user = userHome.findByNationalRegisterNumber((String) value);
		if (user == null || user.equals(userHome.getInstance())) {
			return true;
		}
		return false;
	}

}
