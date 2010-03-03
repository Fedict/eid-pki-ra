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
package be.fedict.eid.pkira.crypto;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jboss.seam.log.Log;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 */
public class CertificateParserImplTest {

	private CertificateParserImpl certificateParser;
	
	@BeforeMethod
	public void setup() {
		certificateParser = new CertificateParserImpl();
		certificateParser.setLog(mock(Log.class));
	}
	
	@Test
	public void testParseCertificate() throws CryptoException {
		CertificateInfo certificateInfo = certificateParser.parseCertificate(TestConstants.VALID_CERTIFICATE);

		assertNotNull(certificateInfo);
		assertEquals(certificateInfo.getSubject(), TestConstants.CERTIFICATE_SUBJECT);
		assertEquals(certificateInfo.getIssuer(), TestConstants.CERTIFICATE_ISSUER);
		
		System.out.println(certificateInfo.getValidityStart().getTime());
		System.out.println(certificateInfo.getValidityEnd().getTime());
		assertEquals(certificateInfo.getValidityStart(), TestConstants.CERTIFICATE_START_DATE);
		assertEquals(certificateInfo.getValidityEnd(), TestConstants.CERTIFICATE_END_DATE);
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseIncompleteCertificate() throws CryptoException {
		certificateParser.parseCertificate(TestConstants.VALID_CERTIFICATE.substring(0, TestConstants.VALID_CERTIFICATE.length() - 10));
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseEmptyCSR() throws CryptoException {
		certificateParser.parseCertificate("");
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseCSRAsCertificate() throws CryptoException {
		certificateParser.parseCertificate(TestConstants.VALID_CSR);
	}
	
	
}
