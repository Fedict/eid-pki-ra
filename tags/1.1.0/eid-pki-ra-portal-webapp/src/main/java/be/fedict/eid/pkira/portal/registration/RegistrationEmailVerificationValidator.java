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

package be.fedict.eid.pkira.portal.registration;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.validator.Validator;


public class RegistrationEmailVerificationValidator implements Validator<RegistrationEmailVerification> {

	@Override
	public boolean isValid(Object value) {
		if (value==null) { 
			return true;
		}
		
		if (value instanceof Registration) {
			Registration registration = (Registration) value;
			return ObjectUtils.equals(registration.getEmailAddress(), registration.getEmailAddressVerification());
		}
		
		return false;
	}

	@Override
	public void initialize(RegistrationEmailVerification parameters) {
		// No work here.
	}

}
