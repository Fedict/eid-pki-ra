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

package be.fedict.eid.pkira.portal.certificaterequest;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.crypto.csr.CSRInfo;
import be.fedict.eid.pkira.crypto.csr.CSRParser;
import be.fedict.eid.pkira.crypto.exception.CryptoException;

/**
 * @author Bram Baeyens
 *
 */
public class CSRUploadTest {
	
	private CSRUpload csrUpload;
	@Mock CSRParser csrParser;
	
	@BeforeMethod
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		csrUpload = new CSRUpload();
		csrUpload.setCsrParser(csrParser);		
	}
	
	@Test
	public void csrAsString() {
		csrUpload.setCsrAsString("abc");
		assertEquals("abc", csrUpload.getBase64Csr());
	}
	
	@Test
	public void csrAsBytes() {
		csrUpload.setCsrAsBytes("def".getBytes());
		assertEquals("def", csrUpload.getBase64Csr());
	}
	
	@Test
	public void extractCsrInfoFromValidCsr() throws Exception {
		CSRInfo csrInfo = new CSRInfo(null);
		when(csrParser.parseCSR(isA(String.class))).thenReturn(csrInfo);
		csrUpload.setCsrAsString("abc");
		assertEquals(csrInfo, csrUpload.extractCsrInfo());
	}
	
	@Test
	public void extractCsrInfoFromInvalidCsr() throws Exception {
		when(csrParser.parseCSR(isA(String.class))).thenThrow(new CryptoException("Invalid CSR"));
		try {
			csrUpload.setCsrAsString("abc");
			csrUpload.extractCsrInfo();
			fail("CryptoException expected.");
		} catch (CryptoException e) {
			assertTrue(true);
		}
	}
}
