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
package be.fedict.eid.pkira.crypto;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.crypto.csr.CSRInfo;
import be.fedict.eid.pkira.crypto.csr.CSRParserImpl;
import be.fedict.eid.pkira.crypto.exception.CryptoException;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Jan Van den Bergh
 */
public class CSRParserImplTest {

	private CSRParserImpl csrParser;
	
	@BeforeMethod
	public void setup() {
		csrParser = new CSRParserImpl();
	}
	
	@Test
	public void testParseCSR() throws CryptoException, IOException {
		CSRInfo csrInfo = csrParser.parseCSR(TestConstants.VALID_CSR);

		assertNotNull(csrInfo);
		assertEquals(csrInfo.getSubject(), TestConstants.CSR_SUBJECT);
		assertNotNull(csrInfo.getDerEncoded());
		assertNotNull(csrInfo.getPemEncoded());
	}
	
	@Test
	public void testParseCRSWithSAN() throws CryptoException {
		CSRInfo csrInfo = csrParser.parseCSR(TestConstants.VALID_CSR_WITH_SAN);
		csrInfo.getSubjectAlternativeNames();
	}

	@Test
	public void testParseCRSWithMultipleExtensionsAnsMultipleSAN() throws CryptoException {
		CSRInfo csrInfo = csrParser.parseCSR(TestConstants.VALID_CSR_WITH_MULTIPLE_SAN);
		List<String> subjectAlternativeNames = csrInfo.getSubjectAlternativeNames();
		assertEquals(2, subjectAlternativeNames.size());
		assertTrue(subjectAlternativeNames.contains("aca-it.be"));
		assertTrue(subjectAlternativeNames.contains("www.aca-it.be"));
	}
	
	@Test(expectedExceptions = CryptoException.class)
	public void testParseCRSWithWildcardInSAN() throws CryptoException {
		CSRInfo csrInfo = csrParser.parseCSR(TestConstants.INVALID_CSR_WILDCARD_IN_SAN);
		csrInfo.getSubjectAlternativeNames();
	}
	
	@Test(expectedExceptions = CryptoException.class)
	public void testParseCSRInvalidSignature() throws CryptoException {
		csrParser.parseCSR(TestConstants.INVALID_CSR);
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseIncompleteCSR() throws CryptoException {
		csrParser.parseCSR(TestConstants.VALID_CSR.substring(0, TestConstants.VALID_CSR.length() - 10));
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseEmptyCSR() throws CryptoException {
		csrParser.parseCSR("");
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseCertificateAsCSR() throws CryptoException {
		csrParser.parseCSR(TestConstants.VALID_CERTIFICATE);
	}
	
	
}
