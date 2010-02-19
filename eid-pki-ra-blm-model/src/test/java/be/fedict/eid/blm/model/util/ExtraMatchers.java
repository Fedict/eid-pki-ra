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

import java.io.Serializable;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

import be.fedict.eid.pkira.contracts.ResponseType;
import be.fedict.eid.pkira.contracts.ResultType;

/**
 * Additional matchers used in the tests.
 * 
 * @author Jan Van den Bergh
 */
public class ExtraMatchers {

	/**
	 * Matcher for response types of the messages.
	 * 
	 * @author Jan Van den Bergh
	 */
	public static class ResponseTypeMatcher extends ArgumentMatcher<ResponseType> implements Serializable {

		private static final long serialVersionUID = 7648824991913516843L;

		private final Class<? extends ResponseType> responseClass;
		private final String requestId;
		private final ResultType resultType;
		private final String resultMessage;

		public ResponseTypeMatcher(Class<? extends ResponseType> responseClass, String requestId,
				ResultType resultType, String resultMessage) {
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
			description.appendText("responseType(" + responseClass.getName() + ", " + requestId + ", " + resultType
					+ ", " + resultMessage + ")");
		}
	}

	/**
	 * Mocking progress used to report mocks.
	 */
	private static MockingProgress mockingProgress = new ThreadSafeMockingProgress();

	/**
	 * Matcher for a JAXB element with a ResponseType in it.	 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ResponseType> JAXBElement<T> isJAXBResponseType(Class<T> responseClass, String requestId,
			ResultType resultType, String resultMessage) {
		return reportMatcher(new ResponseTypeMatcher(responseClass, requestId, resultType, resultMessage))
				.<JAXBElement> returnFor(JAXBElement.class);
	}

	/**
	 * Utility method copied from Mockito.
	 */
	private static HandyReturnValues reportMatcher(ArgumentMatcher<?> matcher) {
		return mockingProgress.getArgumentMatcherStorage().reportMatcher(matcher);
	}
}
