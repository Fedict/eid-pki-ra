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

package be.fedict.eid.pkira.portal.ra;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.annotations.In;
import org.jboss.seam.core.Validators;
import org.jboss.seam.international.StatusMessages;

/**
 * @author Bram Baeyens
 * 
 */
public abstract class WSHome<T> extends PrivateWSQuery {

	private static final long serialVersionUID = -8029624317292385958L;

	@In
	private Validators validators;

	private Object id;
	private T instance;
	private Class<T> entityClass;

	public void setId(Object id) {
		this.id = id;
	}

	public Object getId() {
		return id;
	}

	public void setInstance(T instance) {
		this.instance = instance;
	}

	public T getInstance() {
		if (instance == null) {
			initInstance();
		}
		return instance;
	}

	private void initInstance() {
		if (isIdDefined()) {
			setInstance(find());
		} else {
			setInstance(createInstance());
		}
	}

	public abstract T find();

	public T createInstance() {
		if (getEntityClass() != null) {
			try {
				return getEntityClass().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	private boolean isIdDefined() {
		return id != null;
	}

	public boolean isManaged() {
		return isIdDefined();
	}

	public void validate() throws InvalidStateException {
		ClassValidator<T> validator = validators.getValidator(getEntityClass());
		if (validator.getInvalidValues(getInstance()).length > 0)
			throw new InvalidStateException(validator
					.getInvalidValues(getInstance()));
	}

	public String handleInvalidStateException(InvalidStateException e) {
		for (InvalidValue invalidValue : e.getInvalidValues()) {
			getStatusMessages()
					.addFromResourceBundle(invalidValue.getMessage());
		}
		return null;
	}

	public StatusMessages getStatusMessages() {
		return StatusMessages.instance();
	}

	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;
				if (paramType.getActualTypeArguments().length == 2) {
					if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
						throw new IllegalArgumentException("Could not guess entity class by reflection");
					} else {
						entityClass = (Class<T>) paramType.getActualTypeArguments()[1];
					}
				} else {
					entityClass = (Class<T>) paramType.getActualTypeArguments()[0];
				}
			} else {
				throw new IllegalArgumentException("Could not guess entity class by reflection");
			}
		}
		return entityClass;
	}

}
