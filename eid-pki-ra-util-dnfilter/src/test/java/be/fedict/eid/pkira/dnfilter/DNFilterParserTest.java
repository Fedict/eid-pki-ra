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
public class DNFilterParserTest {

	private DNFilterManagerBean bean;

	@BeforeMethod
	public void setup() {
		bean = new DNFilterManagerBean();
	}

	@Test
	public void testDN1() throws InvalidDNFilterException {
		testDNParser("a=*, c=be, ou=test", "c=be,ou=test,a=*", 3);
	}
	
	@Test
	public void testDN2() throws InvalidDNFilterException {
		testDNParser("a=*, ou=test1, ou=test2", "ou=test1,ou=test2,a=*", 3);
	}

	@Test
	public void testDN3() throws InvalidDNFilterException {
		testDNParser("c=be", "c=be", 1);
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN1() throws InvalidDNFilterException {
		bean.createDNFilter("c=b*");
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN2() throws InvalidDNFilterException {
		bean.createDNFilter("");
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN3() throws InvalidDNFilterException {
		bean.createDNFilter(null);
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN4() throws InvalidDNFilterException {
		bean.createDNFilter("bla");
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN5() throws InvalidDNFilterException {
		bean.createDNFilter("c=be,ou=*,ou=*");
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN6() throws InvalidDNFilterException {
		bean.createDNFilter("c=be,ou=*,cn=*");
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN7() throws InvalidDNFilterException {
		bean.createDNFilter("c=be,000");
	}
	
	@Test(expectedExceptions=InvalidDNFilterException.class)
	public void testInvalidDN8() throws InvalidDNFilterException {
		bean.createDNFilter("{}");
	}

	private void testDNParser(String dn, String normalizedDN, int expectedSize) throws InvalidDNFilterException {
		DNFilter filter = bean.createDNFilter(dn);
		assertEquals(filter.getSize(), expectedSize);
		assertEquals(filter.toString(), normalizedDN);
		
		String normalized = bean.normalize(dn);		
		assertEquals(normalized, normalizedDN);
	}

}
