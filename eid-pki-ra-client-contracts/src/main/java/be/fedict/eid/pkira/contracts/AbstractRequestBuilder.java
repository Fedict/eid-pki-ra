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

import static be.fedict.eid.pkira.contracts.util.JAXBUtil.getObjectFactory;

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
	private String operatorEmail;
	private String operatorFunction;
	private String operatorName;
	private String operatorPhone;
	private final String requestId;

	protected AbstractRequestBuilder() {
		this.requestId = UUID.randomUUID().toString();
	}

	protected AbstractRequestBuilder(String requestId) {
		this.requestId = requestId;
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
	public T setOperatorEmail(String operatorEmail) {
		this.operatorEmail = operatorEmail;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setOperatorFunction(String operatorFunction) {
		this.operatorFunction = operatorFunction;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setOperatorName(String operatorName) {
		this.operatorName = operatorName;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
		return (T) this;
	}

	protected void fillInRequestType(RequestType requestType) {
		requestType.setDescription(description);
		requestType.setLegalNotice(legalNotice);
		requestType.setRequestId(requestId);

		EntityType operator = getObjectFactory().createEntityType();
		operator.setEmail(operatorEmail);
		operator.setFunction(operatorFunction);
		operator.setName(operatorName);
		operator.setPhone(operatorPhone);
		requestType.setOperator(operator);
	}
}
