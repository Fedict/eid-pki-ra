/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
package be.fedict.eid.pkira.dnfilter;

import static be.fedict.eid.pkira.dnfilter.Expressions.INVALID_DNS;
import static be.fedict.eid.pkira.dnfilter.Expressions.INVALID_EXPRESSIONS;
import static be.fedict.eid.pkira.dnfilter.Expressions.NORMALIZED_EXPRESSIONS;
import static be.fedict.eid.pkira.dnfilter.Expressions.VALID_EXPRESSIONS;
import static be.fedict.eid.pkira.dnfilter.Expressions.VALID__SIZES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 */
public class DistinguishedNameExpressionParserTest {

	private DistinguishedNameManagerImpl manager = new DistinguishedNameManagerImpl();

	@Test
	public void testValidExpressions() throws InvalidDistinguishedNameException {
		for (int i = 0; i < VALID_EXPRESSIONS.length; i++) {
			testDNParser(VALID_EXPRESSIONS[i], NORMALIZED_EXPRESSIONS[i], VALID__SIZES[i]);
		}
	}

	@Test
	public void testInvalidExpressions() throws InvalidDistinguishedNameException {
		for (int i = 0; i < INVALID_EXPRESSIONS.length; i++) {
			try {
				manager.createDistinguishedNameExpression(INVALID_EXPRESSIONS[i]);
				fail("Expected InvalidDistinguishedNameException for " + INVALID_EXPRESSIONS[i]);
			} catch (InvalidDistinguishedNameException e) {
				// Ok
			}
		}
	}

	@Test
	public void testInvalidDNs() throws InvalidDistinguishedNameException {
		for (int i = 0; i < INVALID_DNS.length; i++) {
			try {
				manager.createDistinguishedName(INVALID_DNS[i]);
				fail("Expected InvalidDistinguishedNameException for " + INVALID_DNS[i]);
			} catch (InvalidDistinguishedNameException e) {
				// Ok
			}
		}
	}

	private void testDNParser(String dn, String normalizedDN, int expectedSize)
			throws InvalidDistinguishedNameException {
		DistinguishedNameExpression expression = manager.createDistinguishedNameExpression(dn);
		assertEquals(expression.getSize(), expectedSize, dn);
		assertEquals(expression.toString(), normalizedDN, dn);

		String normalized = manager.normalizeDistinguishedNameExpression(dn);
		System.out.println(dn + "/" + normalized);
		assertEquals(normalized, normalizedDN, dn);
	}

}
