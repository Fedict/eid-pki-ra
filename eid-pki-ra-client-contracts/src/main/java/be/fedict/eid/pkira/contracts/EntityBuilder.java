/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
package be.fedict.eid.pkira.contracts;

import be.fedict.eid.pkira.contracts.util.JAXBUtil;
import be.fedict.eid.pkira.generated.contracts.EntityType;


/**
 * @author Jan Van den Bergh
 *
 */
public class EntityBuilder {

	private String operatorFunction;
	private String operatorName;
	private String operatorPhone;	

	public EntityBuilder setFunction(String operatorFunction) {
		this.operatorFunction = operatorFunction;
		return this;
	}

	public EntityBuilder setName(String operatorName) {
		this.operatorName = operatorName;
		return this;
	}

	public EntityBuilder setPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
		return this;
	}

	public EntityType toEntityType() {
		EntityType entity =  JAXBUtil.getObjectFactory().createEntityType();
		entity.setFunction(operatorFunction);
		entity.setName(operatorName);
		entity.setPhone(operatorPhone);
		return entity;
	}
}
