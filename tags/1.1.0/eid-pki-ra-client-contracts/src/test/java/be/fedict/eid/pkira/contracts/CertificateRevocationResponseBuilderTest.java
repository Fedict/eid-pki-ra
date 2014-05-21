/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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
package be.fedict.eid.pkira.contracts;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Jan Van den Bergh
 */
public class CertificateRevocationResponseBuilderTest {

	private static final String RESPONSE_ID = "ResponseId";
	private static final String REQUEST_ID = "RequestId";
	private static final ResultType RESULT = ResultType.GENERAL_FAILURE;
	private static final String RESULT_MESSAGE = "Error";

	public static CertificateRevocationResponseType createFilledCertificateRevocationResponse() {
		CertificateRevocationResponseBuilder builder = new CertificateRevocationResponseBuilder(RESPONSE_ID);
		setOtherFields(builder);
		CertificateRevocationResponseType Response = builder.toResponseType();
		return Response;
	}

	@Test
	public void testCertificateRevocationBuilderWithId() {
		CertificateRevocationResponseType response = createFilledCertificateRevocationResponse();
		
		assertEquals(response.getResponseId(), RESPONSE_ID);
		validateOtherFields(response);
	}	
	
	@Test
	public void testCertificateRevocationBuilderWithoutId() {
		CertificateRevocationResponseBuilder builder = new CertificateRevocationResponseBuilder();
		setOtherFields(builder);
		CertificateRevocationResponseType response = builder.toResponseType();
		
		assertNotNull(response.getResponseId(), RESPONSE_ID);
		validateOtherFields(response);
	}
	
	private static void setOtherFields(CertificateRevocationResponseBuilder builder) {
		builder.setRequestId(REQUEST_ID)
			.setResult(RESULT)
			.setResultMessage(RESULT_MESSAGE);
	}

	private void validateOtherFields(CertificateRevocationResponseType response) {
		assertEquals(response.getRequestId(), REQUEST_ID);
		assertEquals(response.getResult(), RESULT);
		assertEquals(response.getResultMessage(), RESULT_MESSAGE);
	}
}
