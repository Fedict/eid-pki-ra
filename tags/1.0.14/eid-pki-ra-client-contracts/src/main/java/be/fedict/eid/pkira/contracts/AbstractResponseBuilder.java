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

import java.util.UUID;

import be.fedict.eid.pkira.generated.contracts.ResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Base class for the request builders. This takes care of the abstract
 * RequestType fields.
 * 
 * @author Jan Van den Bergh
 */
public abstract class AbstractResponseBuilder<T extends AbstractResponseBuilder<T>> {

	private String requestId;
	private String responseId;
	private ResultType result;
	private String resultMessage;

	protected AbstractResponseBuilder() {
		this.responseId = UUID.randomUUID().toString();
	}

	protected AbstractResponseBuilder(String responseId) {
		this.responseId = responseId;
	}

	@SuppressWarnings("unchecked")
	public T setRequestId(String requestId) {
		this.requestId = requestId;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setResult(ResultType result) {
		this.result = result;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
		return (T) this;
	}

	protected void fillInResponseType(ResponseType response) {
		response.setRequestId(requestId);
		response.setResponseId(responseId);
		response.setResult(result);
		response.setResultMessage(resultMessage);
	}
}
