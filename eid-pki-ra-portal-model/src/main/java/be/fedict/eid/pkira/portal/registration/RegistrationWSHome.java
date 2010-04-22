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

package be.fedict.eid.pkira.portal.registration;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Validators;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.international.StatusMessages;

import be.fedict.eid.pkira.generated.privatews.RegistrationWS;
import be.fedict.eid.pkira.portal.ra.WSHome;

/**
 * @author Bram Baeyens
 *
 */
@Name(RegistrationWSHome.NAME)
public class RegistrationWSHome extends WSHome {

	private static final long serialVersionUID = -4683807097138681240L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.registrationWSHome";
	
	@In(value=RegistrationMapper.NAME, create=true)
	private RegistrationMapper registrationMapper;
	
	@In
	private Validators validators;
	
	private String id;
	private Registration instance;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setInstance(Registration instance) {
		this.instance = instance;
	}

	public Registration getInstance() {
		if (instance == null) {
			initInstance();
		}
		return instance;
	}

	private void initInstance() {
		if (isIdDefined()) {
			setInstance(find());
		} else {
			setInstance(new Registration());
		}
	}

	private boolean isIdDefined() {
		return StringUtils.isNotBlank(id);
	}
	
	public boolean isManaged() {
		return isIdDefined();
	}

	private Registration find() {
		RegistrationWS registrationWS = getServiceClient().findRegistrationById(id);		
		return registrationMapper.map(registrationWS);
	}
	
	public String createOrUpdate() {
		try {
			validate();
			boolean result = getServiceClient().createOrUpdateRegistration(registrationMapper.map(getInstance()));
			if (result) {
				getStatusMessages().addFromResourceBundle(
						StatusMessage.Severity.INFO, isManaged()
								? "registration.updated" : "registration.created");
				return "success";
			} else {
				getStatusMessages().addFromResourceBundle(
						StatusMessage.Severity.ERROR, isManaged()
								? "registration.update.failed" : "registration.create.failed");
				return null;
			}
		} catch (InvalidStateException e) {
			return handleInvalidStateException(e);
		}
	}
	
	private void validate() throws InvalidStateException {
		ClassValidator<Registration> validator = validators.getValidator(Registration.class);
		if (validator.getInvalidValues(getInstance()).length > 0)
			throw new InvalidStateException(validator.getInvalidValues(getInstance()));
	}

	private String handleInvalidStateException(InvalidStateException e) {
		for (InvalidValue invalidValue : e.getInvalidValues()) {
			getStatusMessages().addFromResourceBundle(invalidValue.getMessage());
		}
		return null;
	}

	private StatusMessages getStatusMessages() {
		return StatusMessages.instance();
	}
}
