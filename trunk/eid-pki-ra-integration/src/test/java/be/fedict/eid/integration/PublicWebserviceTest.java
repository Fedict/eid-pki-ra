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
package be.fedict.eid.integration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.publicws.EIDPKIRAServiceClient;

/**
 * Test of the public web service.
 * 
 * @author Jan Van den Bergh
 */
public class PublicWebserviceTest {

	private static final String SERVICE_URL = "http://localhost:8080/eid-pki-ra/webservice/EIDPKIRAService";
	private static final String REQUEST_ID = null;
	private  EIDPKIRAServiceClient webserviceClient;
	private  EIDPKIRAContractsClient contractsClient;

	@BeforeMethod
	public void setup() {
		webserviceClient = new EIDPKIRAServiceClient(SERVICE_URL);
		contractsClient = new EIDPKIRAContractsClient();
	}
	
	@Test
	public void signEmptyString() {
		trySignCertificate("", null, ResultType.INVALID_MESSAGE);
	}
	
	@Test
	public void signCertificateNull() {
		trySignCertificate(null, null, ResultType.INVALID_MESSAGE);
	}
	
	@Test
	public void signEmptyRequest() throws Exception {		
		String requestMessage = "<tns:CertificateRevocationRequest xmlns:tns='urn:be:fedict:eid:pkira:contracts'/>";		
		trySignCertificate(requestMessage, null, ResultType.INVALID_MESSAGE);
	}

	private void trySignCertificate(String requestMessage, String expectedRequestId,
			ResultType expectedResult) {
		// Sign the message
		String responseMsg = webserviceClient.signCertificate(requestMessage);
		
		// Parse the result
		CertificateSigningResponseType response;
		try {
			response = new EIDPKIRAContractsClient().unmarshal(responseMsg,
					CertificateSigningResponseType.class);
		} catch (XmlMarshallingException e) {
			fail("Error unmarshalling response", e);
			return;
		}	
		
		// Validate it
		assertNotNull(response);
		assertEquals(response.getResult(), expectedResult);
		assertEquals(response.getRequestId(), expectedRequestId);
		assertNotNull(response.getResponseId());
	}

}
