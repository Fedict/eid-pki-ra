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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * @author Jan Van den Bergh
 */
public class CertificateSigningResponseBuilderTest {

	private static final String RESPONSE_ID = "ResponseId";
	private static final String REQUEST_ID = "RequestId";
	private static final ResultType RESULT = ResultType.GENERAL_FAILURE;
	private static final String RESULT_MESSAGE = "Error";

	public static CertificateSigningResponseType createFilledCertificateSigningResponse() {
		CertificateSigningResponseBuilder builder = new CertificateSigningResponseBuilder(RESPONSE_ID);
		setOtherFields(builder);
		CertificateSigningResponseType Response = builder.toResponseType();
		return Response;
	}

	@Test
	public void testCertificateSigningBuilderWithId() {
		CertificateSigningResponseType response = createFilledCertificateSigningResponse();
		
		assertEquals(response.getResponseId(), RESPONSE_ID);
		validateOtherFields(response);
	}	
	
	@Test
	public void testCertificateSigningBuilderWithoutId() {
		CertificateSigningResponseBuilder builder = new CertificateSigningResponseBuilder();
		setOtherFields(builder);
		CertificateSigningResponseType response = builder.toResponseType();
		
		assertNotNull(response.getResponseId(), RESPONSE_ID);
		validateOtherFields(response);
	}
	
	private static void setOtherFields(CertificateSigningResponseBuilder builder) {
		builder.setRequestId(REQUEST_ID)
			.setResult(RESULT)
			.setResultMessage(RESULT_MESSAGE);
	}

	private void validateOtherFields(CertificateSigningResponseType response) {
		assertEquals(response.getRequestId(), REQUEST_ID);
		assertEquals(response.getResult(), RESULT);
		assertEquals(response.getResultMessage(), RESULT_MESSAGE);
	}
}
