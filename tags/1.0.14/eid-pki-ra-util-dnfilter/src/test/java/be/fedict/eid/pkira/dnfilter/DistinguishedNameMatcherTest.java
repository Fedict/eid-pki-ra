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

import static be.fedict.eid.pkira.dnfilter.Expressions.MATCHING_DNS;
import static be.fedict.eid.pkira.dnfilter.Expressions.UNMATCHING_DNS;
import static be.fedict.eid.pkira.dnfilter.Expressions.VALID_EXPRESSIONS;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 */
public class DistinguishedNameMatcherTest {

	public DistinguishedNameManagerImpl manager = new DistinguishedNameManagerImpl();

	@Test
	public void testMatching() throws InvalidDistinguishedNameException {
		for (int i = 0; i < VALID_EXPRESSIONS.length; i++) {
			DistinguishedNameExpression expression = manager.createDistinguishedNameExpression(VALID_EXPRESSIONS[i]);
			for(String matchingDn: MATCHING_DNS[i]) {
				DistinguishedName dn = manager.createDistinguishedName(matchingDn);
				assertTrue(expression.matches(dn), VALID_EXPRESSIONS[i] + "/" + matchingDn);
			}
		}
	}
	
	@Test
	public void testUnmatching() throws InvalidDistinguishedNameException {
		for (int i = 0; i < VALID_EXPRESSIONS.length; i++) {
			DistinguishedNameExpression expression = manager.createDistinguishedNameExpression(VALID_EXPRESSIONS[i]);
			for(String unmatchingDn: UNMATCHING_DNS[i]) {
				DistinguishedName dn = manager.createDistinguishedName(unmatchingDn);
				assertFalse(expression.matches(dn), VALID_EXPRESSIONS[i] + "/" + unmatchingDn);
			}
		}
	}
}
