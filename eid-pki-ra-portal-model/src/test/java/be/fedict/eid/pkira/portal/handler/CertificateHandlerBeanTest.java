/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

package be.fedict.eid.pkira.portal.handler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.portal.domain.CSRUpload;
import be.fedict.eid.pkira.portal.domain.CertificateSigningRequest;


/**
 * @author Bram Baeyens
 * 
 */
public class CertificateHandlerBeanTest {

	private static final RequestCertificateHandlerBean HANDLER = new RequestCertificateHandlerBean();
	
	@Mock private Log log;
	@Mock private CSRUpload csrUpload;
	@Mock private FacesMessages facesMessages;
	private CertificateSigningRequest certificate;
	
	@BeforeMethod
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		HANDLER.setLog(log);
		HANDLER.setCsrUpload(csrUpload);
		certificate = new CertificateSigningRequest();
		HANDLER.setSignableCertificate(certificate);
		HANDLER.setFacesMessages(facesMessages);
	}	
	
	@Test
	public void uploadCertificateSigningRequestValid() throws Exception {
		when(csrUpload.extractCsrInfo()).thenReturn(new CSRInfo("testDN"));
		when(csrUpload.getBase64Csr()).thenReturn("testBase64CSR");
		
		String result = HANDLER.uploadCertificateSigningRequest();
		assertEquals("success", result);
		assertEquals("testDN", certificate.getDistinguishedName().getSubject());	
		assertEquals("testBase64CSR", certificate.getBase64Csr());
	}
	
	@Test
	public void uploadCertificateSigningRequestInvalid() throws Exception {
		when(csrUpload.extractCsrInfo()).thenThrow(new CryptoException("Invalid CSR"));
		
		String result = HANDLER.uploadCertificateSigningRequest();
		assertNull(result);
		verify(facesMessages).addFromResourceBundle("validator.csr.invalid");
	}
}
