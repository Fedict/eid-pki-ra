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
package be.fedict.eid.integration.ws;

import static be.fedict.eid.integration.ws.WebServiceFactory.getPublicWebServiceClient;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Test of the public web service.
 * 
 * @author Jan Van den Bergh
 */
public class PublicWebserviceRevocationTest {
	
	@Test
	public void revokeEmptyString() {
		tryRevokeCertificate("", null, ResultType.INVALID_MESSAGE);
	}
	
	@Test
	public void revokeCertificateNull() {
		tryRevokeCertificate(null, null, ResultType.INVALID_MESSAGE);
	}
	
	@Test
	public void revokeCertificateInvalidDN() throws IOException {
		String contract = FileUtils.readFileToString(new File(getClass().getResource("/xml/CertificateRevocationContractInvalidDNStartDateEndDate.xml").getFile()));
		tryRevokeCertificate(contract, "d8b6a3c9-6961-4113-a14a-e2ac00349068", ResultType.INVALID_MESSAGE);
	}
	
	@Test
	public void revokeCertificateValid() throws IOException {
		String contract = FileUtils.readFileToString(new File(getClass().getResource("/xml/CertificateRevocationContractValid.xml").getFile()));
		tryRevokeCertificate(contract, "d8b6a3c9-6961-4113-a14a-e2ac00349068", ResultType.UNKNOWN_CERTIFICATE);
	}
	
	@Test
	public void revokeEmptyRequest() throws Exception {		
		String requestMessage = "<tns:CertificateRevocationRequest xmlns:tns='urn:be:fedict:eid:pkira:contracts'/>";		
		tryRevokeCertificate(requestMessage, null, ResultType.INVALID_MESSAGE);
	}

	private void tryRevokeCertificate(String requestMessage, String expectedRequestId,
			ResultType expectedResult) {
		// Sign the message
		String responseMsg = getPublicWebServiceClient().revokeCertificate(requestMessage);
		System.out.println(responseMsg);
		
		// Parse the result
		CertificateRevocationResponseType response;
		try {
			response = new EIDPKIRAContractsClient().unmarshal(responseMsg,
					CertificateRevocationResponseType.class);
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
