/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

package be.fedict.eid.pkira.portal.certificaterequest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.crypto.csr.CSRInfo;
import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.portal.certificate.CertificateType;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;


/**
 * @author Bram Baeyens
 * 
 */
public class CSRUploadHandlerBeanTest {

	private final CSRUploadHandlerBean handler = new CSRUploadHandlerBean();
	
	@Mock private Log log;
	@Mock private CSRUpload csrUpload;
	@Mock private FacesMessages facesMessages;
	@Mock private CSRInfo csrInfo;
	@Mock private DistinguishedNameManager distinguishedNameManager;
	@Mock private EIDPKIRAPrivateServiceClient eidpkiraPrivateServiceClient;
	@Mock private EIdUserCredentials credentials;
	@Mock private Map<String, String> messages;
	
	private RequestContract contract;
	
	@BeforeMethod
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		handler.setLog(log);
		handler.setCsrUpload(csrUpload);
		contract = new RequestContract();
		contract.setMessages(messages);
		handler.setRequestContract(contract);
		handler.setFacesMessages(facesMessages);
		handler.setDistinguishedNameManager(distinguishedNameManager);
		handler.setEidpkiraPrivateServiceClient(eidpkiraPrivateServiceClient);
		handler.setCredentials(credentials);
		
		when(credentials.getUsername()).thenReturn("USER");
	}	
	
	@Test
	public void uploadCertificateSigningRequestValid() throws Exception {
		when(csrUpload.extractCsrInfo()).thenReturn(csrInfo);
		when(csrInfo.getSubject()).thenReturn("testDN");
		when(csrUpload.getBase64Csr()).thenReturn("testBase64CSR");
		when(eidpkiraPrivateServiceClient.getAllowedCertificateTypes("USER", "testDN", new ArrayList<String>())).thenReturn(Collections.singletonList(CertificateTypeWS.CODE));
		
		String result = handler.uploadCertificateSigningRequest();
		assertEquals("success", result);
		assertEquals("testDN", contract.getDistinguishedName());	
		assertEquals("testBase64CSR", contract.getBase64Csr());
		assertEquals(1, contract.getAllowedCertificateTypes().size());
		assertEquals(CertificateType.CODE, contract.getAllowedCertificateTypes().get(0).getValue());
		assertEquals(CertificateType.CODE, contract.getCertificateType());
	}
	
	@Test
	public void uploadCertificateSigningRequestInvalid() throws Exception {
		when(csrUpload.extractCsrInfo()).thenThrow(new CryptoException("Invalid CSR"));
		
		String result = handler.uploadCertificateSigningRequest();
		assertNull(result);
		verify(facesMessages).addFromResourceBundle("validator.invalid.csr");
	}
}
