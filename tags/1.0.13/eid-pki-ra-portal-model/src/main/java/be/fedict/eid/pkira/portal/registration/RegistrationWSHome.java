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

package be.fedict.eid.pkira.portal.registration;

import org.hibernate.validator.InvalidStateException;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessage;

import be.fedict.eid.pkira.generated.privatews.RegistrationWS;
import be.fedict.eid.pkira.portal.framework.WSHome;

/**
 * @author Bram Baeyens
 *
 */
@Name(RegistrationWSHome.NAME)
public class RegistrationWSHome extends WSHome<Registration> {

	private static final long serialVersionUID = -4683807097138681240L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.registrationWSHome";
	
	@In(value=RegistrationMapper.NAME, create=true)
	private RegistrationMapper registrationMapper;

	public Registration find() {
		RegistrationWS registrationWS = getServiceClient().findRegistrationById((String) getId());		
		return registrationMapper.map(registrationWS);
	}
	
	public String createOrUpdate() {
		try {
			validate();
			boolean result = getServiceClient().createOrUpdateRegistration(registrationMapper.map(getInstance()));
			if (result) {
				getStatusMessages().addFromResourceBundle(StatusMessage.Severity.INFO, isManaged()
								? "registration.updated" : "registration.created");
				return "success";
			} else {
				getStatusMessages().addFromResourceBundle(StatusMessage.Severity.ERROR, isManaged()
								? "registration.update.failed" : "registration.create.failed");
				return null;
			}
		} catch (InvalidStateException e) {
			return handleInvalidStateException(e);
		}
	}
}
