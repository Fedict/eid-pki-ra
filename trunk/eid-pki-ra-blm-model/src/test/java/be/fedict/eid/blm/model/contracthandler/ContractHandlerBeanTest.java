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
package be.fedict.eid.blm.model.contracthandler;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.blm.model.contracthandler.ContractHandlerBean;
import be.fedict.eid.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.blm.model.contracthandler.ContractParser;
import be.fedict.eid.blm.model.contracthandler.util.ResponseTypeMatcher;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningResponseBuilder;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.RequestType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Test for the Contract Parser.
 * 
 * @author Jan Van den Bergh
 */
public class ContractHandlerBeanTest {

	private static final String MSG_INVALID_SIGNATURE = "Invalid signature";

	private static final String REQUEST_ID = "TEST-REQUEST-1";
	private static final String REQUEST_MESSAGE = "Test message";
	private static final String RESPONSE_MESSAGE = "Test message 2";

	private ContractHandlerBean contractHandler;
	private ContractParser contractParser;

	@BeforeMethod
	public void setup() {
		contractParser = mock(ContractParser.class);

		contractHandler = new ContractHandlerBean();
		contractHandler.contractParser = contractParser;
	}

	@Test
	public void testFillResponseFromRequest() {
		RequestType request = createEmptySigningRequest();

		CertificateSigningResponseBuilder responseBuilder = new CertificateSigningResponseBuilder();
		contractHandler.fillResponseFromRequest(responseBuilder, request, ResultType.BACKEND_ERROR, RESPONSE_MESSAGE);
		CertificateSigningResponseType response = responseBuilder.toResponseType();

		assertEquals(response.getRequestId(), REQUEST_ID);
		assertEquals(response.getResult(), ResultType.BACKEND_ERROR);
		assertEquals(response.getResultMessage(), RESPONSE_MESSAGE);
		assertNotNull(response.getResponseId());
	}

	@Test
	public void testSignCertificateUnmarshalError() throws ContractHandlerBeanException {
		// Setup test
		ResponseTypeMatcher<CertificateSigningResponseType> matcher = new ResponseTypeMatcher<CertificateSigningResponseType>(
				CertificateSigningResponseType.class, null, ResultType.INVALID_SIGNATURE, MSG_INVALID_SIGNATURE);

		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenThrow(new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, MSG_INVALID_SIGNATURE));
		when(contractParser.marshalResponseMessage(argThat(matcher), eq(CertificateSigningResponseType.class))).thenReturn(
				RESPONSE_MESSAGE);

		// Run it
		String result = contractHandler.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verify(contractParser).unmarshalRequestMessage(REQUEST_MESSAGE, CertificateSigningRequestType.class);
		verify(contractParser).marshalResponseMessage(argThat(matcher), eq(CertificateSigningResponseType.class));
		verifyNoMoreInteractions(contractParser);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateHappyFlow() throws ContractHandlerBeanException {
		ResponseTypeMatcher<CertificateSigningResponseType> matcher = new ResponseTypeMatcher<CertificateSigningResponseType>(
				CertificateSigningResponseType.class, REQUEST_ID, ResultType.GENERAL_FAILURE, "Not implemented");

		// Setup test
		CertificateSigningRequestType request = createEmptySigningRequest();
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(request);
		when(contractParser.marshalResponseMessage(argThat(matcher), eq(CertificateSigningResponseType.class))).thenReturn(
				RESPONSE_MESSAGE);

		// Run it
		String result = contractHandler.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verify(contractParser).unmarshalRequestMessage(REQUEST_MESSAGE, CertificateSigningRequestType.class);
		verify(contractParser).marshalResponseMessage(argThat(matcher), eq(CertificateSigningResponseType.class));
		verifyNoMoreInteractions(contractParser);
		
		assertEquals(result, RESPONSE_MESSAGE);
	}

	private CertificateSigningRequestType createEmptySigningRequest() {
		return new CertificateSigningRequestBuilder(REQUEST_ID).toRequestType();
	}
}
