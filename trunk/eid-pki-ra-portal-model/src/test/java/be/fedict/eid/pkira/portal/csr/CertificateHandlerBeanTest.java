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

package be.fedict.eid.pkira.portal.csr;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CryptoException;


/**
 * @author Bram Baeyens
 * 
 */
public class CertificateHandlerBeanTest {

	private static final CertificateHandlerBean HANDLER = new CertificateHandlerBean();
	
	@Test
	public void uploadCertificateSigningRequestValid() throws Exception {
		Log log = mock(Log.class);
		HANDLER.setLog(log);
		
		CSRParser csrParser = mock(CSRParser.class);
		when(csrParser.parseCSR(isA(String.class))).thenReturn(new CSRInfo("testDN"));
		HANDLER.setCsrParser(csrParser);
		
		CertificateSigningRequest certificateSigningRequest = new CertificateSigningRequest();
		certificateSigningRequest.setCsr(new byte[] {'a', 'b', 'c'});
		
		HANDLER.setCertificateSigningRequest(certificateSigningRequest);
		String result = HANDLER.uploadCertificateSigningRequest();
		Assert.assertEquals("success", result);
		Assert.assertEquals("testDN", certificateSigningRequest.getDistinguishedName().getSubject());		
	}
	
	@Test
	public void uploadCertificateSigningRequestInvalid() throws Exception {
		Log log = mock(Log.class);
		HANDLER.setLog(log);
		
		CSRParser csrParser = mock(CSRParser.class);
		when(csrParser.parseCSR(isA(String.class))).thenThrow(new CryptoException("Invalid CSR"));
		HANDLER.setCsrParser(csrParser);
		
		FacesMessages facesMessages = mock(FacesMessages.class);
		HANDLER.setFacesMessages(facesMessages);
		
		CertificateSigningRequest certificateSigningRequest = new CertificateSigningRequest();
		certificateSigningRequest.setCsr(new byte[] {'a', 'b', 'c'});
		
		HANDLER.setCertificateSigningRequest(certificateSigningRequest);
		String result = HANDLER.uploadCertificateSigningRequest();
		Assert.assertNull(result);
		verify(facesMessages).addFromResourceBundle("validator.csr.invalid");

	}
}
