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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 */
public class CSRParserTest {

	@Test
	public void testParseCSR() throws CryptoException {
		CSRInfo csrInfo = CSRParser.parseCSR(TestConstants.VALID_CSR);

		assertNotNull(csrInfo);
		assertEquals(csrInfo.getSubject(), TestConstants.CSR_SUBJECT);
	}
	
	@Test(expectedExceptions = CryptoException.class)
	public void testParseCSRInvalidSignature() throws CryptoException {
		CSRParser.parseCSR(TestConstants.INVALID_CSR);
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseIncompleteCSR() throws CryptoException {
		CSRParser.parseCSR(TestConstants.VALID_CSR.substring(0, TestConstants.VALID_CSR.length() - 10));
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseEmptyCSR() throws CryptoException {
		CSRParser.parseCSR("");
	}

	@Test(expectedExceptions = CryptoException.class)
	public void testParseCertificateAsCSR() throws CryptoException {
		CSRParser.parseCSR(TestConstants.VALID_CERTIFICATE);
	}
	
	
}
