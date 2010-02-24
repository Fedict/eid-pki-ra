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
package be.fedict.eid.blm.model.util;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import be.fedict.eid.pkira.generated.contracts.ResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Matcher for response types of the messages.
 * 
 * @author Jan Van den Bergh
 */
public class ResponseTypeMatcher<T extends ResponseType> extends ArgumentMatcher<JAXBElement<T>> {

	private final Class<? extends ResponseType> responseClass;
	private final String requestId;
	private final ResultType resultType;
	private final String resultMessage;

	public ResponseTypeMatcher(Class<T> responseClass, String requestId, ResultType resultType, String resultMessage) {
		this.responseClass = responseClass;
		this.requestId = requestId;
		this.resultType = resultType;
		this.resultMessage = resultMessage;
	}

	public boolean matches(Object actual) {
		if (actual != null && actual instanceof JAXBElement<?>) {
			JAXBElement<?> element = (JAXBElement<?>) actual;

			if (element.getDeclaredType() != responseClass) {
				return false;
			}

			ResponseType response = (ResponseType) element.getValue();

			return (response != null) && StringUtils.equals(requestId, response.getRequestId())
					&& response.getResponseId() != null && ObjectUtils.equals(resultType, response.getResult())
					&& StringUtils.equals(resultMessage, response.getResultMessage());
		}

		return false;
	}

	public void describeTo(Description description) {
		description.appendText("responseType(" + responseClass.getName() + ", " + requestId + ", " + resultType + ", "
				+ resultMessage + ")");
	}
}
