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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 */
public class DistinguishedNameMatcherTest {

	public DistinguishedNameManagerImpl manager = new DistinguishedNameManagerImpl();

	@Test
	public void testMatching1() throws InvalidDistinguishedNameException {
		String expr = "c=be,CN=test";
		assertTrue(matches(expr, "cn=test, c=be"));
		assertTrue(matches(expr, "c=be, CN=test"));
		assertFalse(matches(expr, "c=be,CN=Test"));
	}

	@Test
	public void testMatching2() throws InvalidDistinguishedNameException {
		String expr = "cn=Test,ou=*";
		assertTrue(matches(expr, "cn=Test,ou=a"));
		assertTrue(matches(expr, "ou=b,cn=Test"));
	}

	@Test
	public void testMatching3() throws InvalidDistinguishedNameException {
		String expr = "u=*,ou=*";
		assertTrue(matches(expr, "U=Test,ou=a"));
		assertTrue(matches(expr, "ou=b,u=Test"));
		assertFalse(matches(expr, "ou=a,u=Test,u=Test"));
	}
	
	@Test
	public void testMatching4() throws InvalidDistinguishedNameException {
		String expr = "CN=Test,ou=a,ou=b,C=be";
		assertTrue(matches(expr, "CN=Test,ou=a,ou=b,C=be"));
		assertTrue(matches(expr, "C=be,ou=a,ou=b,CN=Test"));
		assertFalse(matches(expr, "CN=Test,ou=b,ou=a,C=be"));
	}
	
	@Test
	public void testMatching5() throws InvalidDistinguishedNameException {
		assertTrue(matches("c=be,ou=abc,ou=def,cn=*", "cn=test,c=be,ou=abc,ou=def"));
	}

	@Test
	public void testMatching6() throws InvalidDistinguishedNameException {
		assertTrue(matches("c=*,ou=*,cn=*", "cn=test,c=test,ou=test"));
	}

	private boolean matches(String filter1, String filter2) throws InvalidDistinguishedNameException {
		return manager.createDistinguishedName(filter1).matches(manager.createDistinguishedName(filter2));
	}
}
