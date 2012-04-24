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
package be.fedict.eid.pkira.contracts;

import java.util.UUID;

import be.fedict.eid.pkira.generated.contracts.EntityType;
import be.fedict.eid.pkira.generated.contracts.RequestType;

/**
 * Base class for the request builders. This takes care of the abstract
 * RequestType fields.
 * 
 * @author Jan Van den Bergh
 */
public abstract class AbstractRequestBuilder<T extends AbstractRequestBuilder<T>> {

	private String description;
	private String legalNotice;
	private final String requestId;
	private EntityType operator;

	protected AbstractRequestBuilder() {
		this.requestId = UUID.randomUUID().toString();
	}

	protected AbstractRequestBuilder(String requestId) {
		if (requestId == null) {
			this.requestId = UUID.randomUUID().toString();
		} else {
			this.requestId = requestId;
		}
	}

	@SuppressWarnings("unchecked")
	public T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setLegalNotice(String legalNotice) {
		this.legalNotice = legalNotice;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setOperator(EntityType operator) {
		this.operator = operator;
		return (T) this;
	}

	protected void fillInRequestType(RequestType requestType) {
		requestType.setId("request");
		requestType.setDescription(description);
		requestType.setLegalNotice(legalNotice);
		requestType.setRequestId(requestId);
		requestType.setOperator(operator);
	}
}
