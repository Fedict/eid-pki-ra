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
package be.fedict.eid.pkira.blm.model.contracthandler;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.jboss.seam.log.Log;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.contracts.EIDPKIRAContractsClient;
import be.fedict.eid.pkira.contracts.XmlMarshallingException;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;

/**
 * Test for the Contract Parser.
 * 
 * @author Jan Van den Bergh
 */
public class ContractParserTest {

	private ContractParserBean bean;
	private Log log;
	private EIDPKIRAContractsClient contractsClient;

	@BeforeTest
	public void setup() {
		log = mock(Log.class);
		contractsClient = mock(EIDPKIRAContractsClient.class);
		
		bean = new ContractParserBean();				
		bean.setLog(log);
		bean.setContractsClient(contractsClient);
	}

	@Test
	public void testMarshalMessage() throws Exception {
		CertificateSigningResponseType responseType = new CertificateSigningResponseType();
		when(contractsClient.marshal(eq(responseType), eq(CertificateSigningResponseType.class))).thenThrow(new XmlMarshallingException());		
		
		String response = bean.marshalResponseMessage(responseType, CertificateSigningResponseType.class);
		assertNull(response);
		verify(log).error(anyObject(), isA(XmlMarshallingException.class));
	}
	
	@Test
	public void testMarshalMessageError() throws Exception {
		CertificateSigningResponseType responseType = new CertificateSigningResponseType();
		String expectedResponse = "RESPONSE";		
		when(contractsClient.marshal(eq(responseType), eq(CertificateSigningResponseType.class))).thenReturn(expectedResponse );		
		
		String response = bean.marshalResponseMessage(responseType, CertificateSigningResponseType.class);
		
		assertEquals(response, expectedResponse);
	}

	@Test
	public void testUnmarshalSigningRequest() throws ContractHandlerBeanException, XmlMarshallingException {
		String requestMsg = "REQUEST";
		CertificateSigningRequestType expectedRequestType=new CertificateSigningRequestType();
		when(contractsClient.unmarshal(eq(requestMsg), eq(CertificateSigningRequestType.class))).thenReturn(expectedRequestType);		
		
		CertificateSigningRequestType requestType = bean.unmarshalRequestMessage(requestMsg , CertificateSigningRequestType.class);
		assertEquals(requestType, expectedRequestType);
	}
	
	@Test(expectedExceptions=ContractHandlerBeanException.class)
	public void testUnmarshalSigningRequestError() throws ContractHandlerBeanException, XmlMarshallingException {
		String requestMsg = "REQUEST";
		when(contractsClient.unmarshal(eq(requestMsg), eq(CertificateSigningRequestType.class))).thenThrow(new XmlMarshallingException());		
		
		bean.unmarshalRequestMessage(requestMsg , CertificateSigningRequestType.class);		
	}
}
