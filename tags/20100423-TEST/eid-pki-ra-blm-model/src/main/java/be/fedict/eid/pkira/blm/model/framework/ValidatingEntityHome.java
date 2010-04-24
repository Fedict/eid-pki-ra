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

package be.fedict.eid.pkira.blm.model.framework;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.annotations.In;
import org.jboss.seam.core.Validators;
import org.jboss.seam.framework.EntityHome;

/**
 * @author Bram Baeyens
 * 
 */
public abstract class ValidatingEntityHome<E> extends EntityHome<E> {

	private static final long serialVersionUID = -1166625616633823474L;
	
	@In
	private Validators validators;

	@Override
	public String persist() {
		try {
			validate();
			return super.persist();
		} catch (InvalidStateException e) {
			return handleInvalidStateException(e);
		}
	}

	@Override
	public String update() {
		try {
			validate();
			return super.update();
		} catch (InvalidStateException e) {
			return handleInvalidStateException(e);
		}
	}

	private void validate() throws InvalidStateException {
		ClassValidator<E> validator = validators.getValidator(getEntityClass());
		if (validator.getInvalidValues(getInstance()).length > 0)
			throw new InvalidStateException(validator.getInvalidValues(getInstance()));
	}

	private String handleInvalidStateException(InvalidStateException e) {
		for (InvalidValue invalidValue : e.getInvalidValues()) {
			addFacesMessageFromResourceBundle(invalidValue.getMessage());
		}
		return null;
	}
}
