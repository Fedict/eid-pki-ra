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

import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 */
public class DistinguishedNameOverlapsTest {

	DistinguishedNameManagerImpl manager = new DistinguishedNameManagerImpl();

	@Test
	public void testOverlaps1() throws InvalidDistinguishedNameException {
		testOverlap(true, "c=be", "C=be");
		testOverlap(false, "c=be", "c=nl");
		testOverlap(false, "c=be", "ou=be");
		testOverlap(false, "c=be", "C=BE");
		
		testOverlap(true, "ou=*", "ou=abc");
		testOverlap(false, "ou=*", "ou=abc,ou=def");
		testOverlap(false, "ou=*", "ou=abc,ou=*");
		testOverlap(true, "ou=*", "ou=*");
		testOverlap(true, "ou=*,ou=*", "ou=abc,ou=abc");
		testOverlap(true, "ou=*,ou=*", "ou=*,ou=abc");
		testOverlap(true, "ou=*,ou=*", "ou=*,ou=abc");
		testOverlap(false, "ou=*,ou=*", "ou=*");
		testOverlap(false, "ou=*,ou=*", "ou=abc");
		
		testOverlap(true, "*ou=*,ou=abc", "ou=abc");
		testOverlap(true, "*ou=*,ou=abc", "ou=abc,ou=abc");
		testOverlap(true, "*ou=*,ou=abc", "ou=def,ou=*");
		
		testOverlap(true, "*ou=*,ou=*", "ou=abc");
		testOverlap(true, "*ou=*,ou=*", "ou=*");
		testOverlap(true, "*ou=*,ou=*", "ou=abc,ou=*");
		testOverlap(true, "*ou=*,ou=*", "ou=abc,ou=*,*ou=*");
		testOverlap(false, "*ou=*,ou=*", "ou=*,c=*");
		testOverlap(true, "*ou=*,ou=*", "ou=*,*c=*");
		
		testOverlap(true, "*ou=*", "ou=abc");
		testOverlap(true, "*ou=*", "ou=abc,ou=def");
		testOverlap(true, "*ou=*", "ou=abc,ou=*,ou=def");

		testOverlap(true, "cn=Test,ou=a,ou=*,*cn=*,cn=bla", "cn=Test,ou=*,ou=*,cn=test,cn=test,cn=*,cn=*");
	}

	private void testOverlap(boolean expectedOverlap, String firstDN, String otherDN)
			throws InvalidDistinguishedNameException {
		DistinguishedNameExpression expr1 = manager.createDistinguishedNameExpression(firstDN);
		DistinguishedNameExpression expr2 = manager.createDistinguishedNameExpression(otherDN);

		assertEquals(expr1.overlaps(expr2), expectedOverlap, firstDN + "/" + otherDN);
		assertEquals(expr2.overlaps(expr1), expectedOverlap, otherDN + "/" + firstDN);
	}
}
