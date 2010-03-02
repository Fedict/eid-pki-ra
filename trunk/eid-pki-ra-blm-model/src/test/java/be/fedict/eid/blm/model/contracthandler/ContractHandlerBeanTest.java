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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.blm.model.contracthandler.util.ResponseTypeMatcher;
import be.fedict.eid.blm.model.eiddss.SignatureVerification;
import be.fedict.eid.blm.model.validation.FieldValidator;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningResponseBuilder;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Test for the Contract Parser.
 * 
 * @author Jan Van den Bergh
 */
public class ContractHandlerBeanTest {

	/**
	 * 
	 */
	private static final String MSG_INVALID_SIGNATURE = "Invalid signature";
	private static final String MSG_NOT_IMPLEMENTED = "Not implemented";
	private static final String MSG_VALIDATION_FAILURE = "Validation failure";
	private static final String MSG_INVALID_MESSAGE = "Invalid message";
	private static final String REQUEST_ID = "TEST-REQUEST-1";
	private static final String REQUEST_MESSAGE = "Test message";
	private static final String RESPONSE_MESSAGE = "Test message 2";
	private static final String SIGNER = "69123110110";

	private ContractHandlerBean contractHandler;
	private ContractParser contractParser;
	private FieldValidator fieldValidator;
	private SignatureVerification signatureVerification;

	private ResponseTypeMatcher<CertificateSigningResponseType> notImplementedMatcher;
	private ResponseTypeMatcher<CertificateSigningResponseType> validationFailureMatcher;
	private ResponseTypeMatcher<CertificateSigningResponseType> invalidMessageMatcher;
	private ResponseTypeMatcher<CertificateSigningResponseType> invalidSignatureMatcher;

	private CertificateSigningRequestType request;

	@BeforeMethod
	public void setup() {
		// Setup beans
		contractParser = mock(ContractParser.class);
		fieldValidator = mock(FieldValidator.class);
		signatureVerification = mock(SignatureVerification.class);

		contractHandler = new ContractHandlerBean();
		contractHandler.contractParser = contractParser;
		contractHandler.fieldValidator = fieldValidator;
		contractHandler.signatureVerification = signatureVerification;

		// Setup other objects
		request = createEmptySigningRequest();
		notImplementedMatcher = new ResponseTypeMatcher<CertificateSigningResponseType>(
				CertificateSigningResponseType.class, REQUEST_ID, ResultType.GENERAL_FAILURE, MSG_NOT_IMPLEMENTED);
		validationFailureMatcher = new ResponseTypeMatcher<CertificateSigningResponseType>(
				CertificateSigningResponseType.class, REQUEST_ID, ResultType.INVALID_MESSAGE, MSG_VALIDATION_FAILURE);
		invalidMessageMatcher = new ResponseTypeMatcher<CertificateSigningResponseType>(
				CertificateSigningResponseType.class, null, ResultType.INVALID_MESSAGE, MSG_INVALID_MESSAGE);
		invalidSignatureMatcher = new ResponseTypeMatcher<CertificateSigningResponseType>(
				CertificateSigningResponseType.class, REQUEST_ID, ResultType.INVALID_SIGNATURE, MSG_INVALID_SIGNATURE);
	}

	@Test
	public void testFillResponseFromRequest() {
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
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, MSG_INVALID_MESSAGE));
		when(
				contractParser.marshalResponseMessage(argThat(invalidMessageMatcher),
						eq(CertificateSigningResponseType.class))).thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = contractHandler.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verify(contractParser).unmarshalRequestMessage(REQUEST_MESSAGE, CertificateSigningRequestType.class);
		verify(contractParser).marshalResponseMessage(argThat(invalidMessageMatcher),
				eq(CertificateSigningResponseType.class));
		verifyNoMoreInteractions();
		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateValidationFailure() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(request);
		when(
				contractParser.marshalResponseMessage(argThat(validationFailureMatcher),
						eq(CertificateSigningResponseType.class))).thenReturn(RESPONSE_MESSAGE);
		doThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, MSG_VALIDATION_FAILURE)).when(
				fieldValidator).validateContract(eq(request));

		// Run it
		String result = contractHandler.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verify(contractParser).unmarshalRequestMessage(REQUEST_MESSAGE, CertificateSigningRequestType.class);
		verify(contractParser).marshalResponseMessage(argThat(validationFailureMatcher),
				eq(CertificateSigningResponseType.class));
		verify(fieldValidator).validateContract(eq(request));
		verifyNoMoreInteractions();

		assertEquals(result, RESPONSE_MESSAGE);
	}
	
	@Test
	public void testSignCertificateInvalidSignature() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(request);
		when(
				contractParser.marshalResponseMessage(argThat(invalidSignatureMatcher),
						eq(CertificateSigningResponseType.class))).thenReturn(RESPONSE_MESSAGE);
		when(signatureVerification.verifySignature(eq(REQUEST_MESSAGE))).thenThrow(new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, MSG_INVALID_SIGNATURE));

		// Run it
		String result = contractHandler.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verify(contractParser).unmarshalRequestMessage(REQUEST_MESSAGE, CertificateSigningRequestType.class);
		verify(contractParser).marshalResponseMessage(argThat(invalidSignatureMatcher),
				eq(CertificateSigningResponseType.class));
		verify(fieldValidator).validateContract(eq(request));
		verify(signatureVerification).verifySignature(eq(REQUEST_MESSAGE));
		verifyNoMoreInteractions();

		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateHappyFlow() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(request);
		when(
				contractParser.marshalResponseMessage(argThat(notImplementedMatcher),
						eq(CertificateSigningResponseType.class))).thenReturn(RESPONSE_MESSAGE);
		when(signatureVerification.verifySignature(eq(REQUEST_MESSAGE))).thenReturn(SIGNER);

		// Run it
		String result = contractHandler.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verify(contractParser).unmarshalRequestMessage(REQUEST_MESSAGE, CertificateSigningRequestType.class);
		verify(contractParser).marshalResponseMessage(argThat(notImplementedMatcher),
				eq(CertificateSigningResponseType.class));
		verify(fieldValidator).validateContract(eq(request));
		verify(signatureVerification).verifySignature(eq(REQUEST_MESSAGE));
		verifyNoMoreInteractions();

		assertEquals(result, RESPONSE_MESSAGE);
	}

	private void verifyNoMoreInteractions() {
		Mockito.verifyNoMoreInteractions(contractParser, fieldValidator, signatureVerification);
	}

	private CertificateSigningRequestType createEmptySigningRequest() {
		return new CertificateSigningRequestBuilder(REQUEST_ID).toRequestType();
	}
}
