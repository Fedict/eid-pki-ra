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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 *
 */
public class DistinguishedNameParserTest {

	private DistinguishedNameManagerImpl manager;

	@BeforeMethod
	public void setup() {
		manager = new DistinguishedNameManagerImpl();
	}

	@Test
	public void testDN1() throws InvalidDistinguishedNameException {
		testDNParser("a=*, mail=test@test.be, c=be", "a=*,c=be,mail=test@test.be", 3);
	}
	
	@Test
	public void testDN2() throws InvalidDistinguishedNameException {
		testDNParser("CN=*, a=test 1, ou=test2", "a=test 1,cn=*,ou=test2", 3);
	}

	@Test
	public void testDN3() throws InvalidDistinguishedNameException {
		testDNParser("c=be", "c=be", 1);
	}
	
	@Test
	public void testDN4() throws InvalidDistinguishedNameException {
		testDNParser("c=be,ou=*,cn=*", "c=be,cn=*,ou=*", 3);
	}
	
	@Test
	public void testDN5() throws InvalidDistinguishedNameException {
		testDNParser("c=be,ou=xyz,ou=abc", "c=be,ou=xyz,ou=abc", 3);
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN1() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("c=b*c");
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN2() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("");
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN3() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName(null);
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN4() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("bla");
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN5() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("c=be,ou=*,ou=*");
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN6() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("c=be,ou=*,ou=*");
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN7() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("c=be,000");
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN8() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("{}");
	}
	
	@Test(expectedExceptions=InvalidDistinguishedNameException.class)
	public void testInvalidDN9() throws InvalidDistinguishedNameException {
		manager.createDistinguishedName("c=be,ou=*,ou=a");
	}

	private void testDNParser(String dn, String normalizedDN, int expectedSize) throws InvalidDistinguishedNameException {
		DistinguishedName filter = manager.createDistinguishedName(dn);
		assertEquals(filter.getSize(), expectedSize);
		assertEquals(filter.toString(), normalizedDN);
		
		String normalized = manager.normalize(dn);		
		assertEquals(normalized, normalizedDN);
	}

}
