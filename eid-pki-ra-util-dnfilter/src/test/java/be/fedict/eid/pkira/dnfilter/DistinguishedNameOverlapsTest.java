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
package be.fedict.eid.pkira.dnfilter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 */
public class DistinguishedNameOverlapsTest {

	DistinguishedNameManagerImpl manager = new DistinguishedNameManagerImpl();

	@Test
	public void testOverlaps1() {
		String expr = "cn=Test,ou=*";
		testOverlap(1, "cn=Test,ou=a", expr);
		testOverlap(0, "cn=test,ou=a", expr);
	}
	
	public void testOverlaps2() {
		String expr1 = "c=be,ou=Test,cn=*";
		String expr2 = "c=be,ou=*,cn=Test2";
		
		testOverlap(2, "c=be,ou=Test,cn=Test2", expr1, expr2);
		testOverlap(0, "c=be,ou=Test2,cn=Test", expr1, expr2);
	}

	private void testOverlap(int expectedOverlaps, String firstDN, String... otherDNs) {
		assertEquals(manager.overlapsWith(dn(firstDN), dns(otherDNs)).size(), expectedOverlaps);
	}
	
	private List<DistinguishedName> dns(String... expressions) {
		List<DistinguishedName> result = new ArrayList<DistinguishedName>();
		for (String expression : expressions) {
			result.add(dn(expression));
		}

		return result;
	}

	private DistinguishedName dn(String expression) {
		try {
			return manager.createDistinguishedName(expression);
		} catch (InvalidDistinguishedNameException e) {
			fail("Invalid DN:" + expression);
			return null;
		}
	}
}
