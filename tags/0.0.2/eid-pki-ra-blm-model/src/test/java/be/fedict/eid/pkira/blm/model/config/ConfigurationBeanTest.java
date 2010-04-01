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
package be.fedict.eid.pkira.blm.model.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;


/**
 * @author Jan Van den Bergh
 *
 */
public class ConfigurationBeanTest extends DatabaseTest {

	private static final String VALUE = " value ABC";
	private static final String VALUE2 = "value DEF";
	private static final String VALID_KEY = "key";
	private static final String UNKNOWN_KEY = "unknown";
	private ConfigurationBean bean;
	
	@BeforeMethod
	public void setup() {
		bean = new ConfigurationBean();
		bean.setEntityManager(getEntityManager());
	}
	
	@Test
	public void testSetAndGet() {
		forceCommit();
		
		bean.setConfigurationValue(VALID_KEY, VALUE);		
		assertEquals(bean.getConfigurationValue(VALID_KEY), VALUE);
	}
	
	@Test(dependsOnMethods="testSetAndGet")
	public void testUpdateAndGet() {
		bean.setConfigurationValue(VALID_KEY, VALUE2);		
		assertEquals(bean.getConfigurationValue(VALID_KEY), VALUE2);
	}
	
	@Test
	public void testGetUnknownKey() {
		assertNull(bean.getConfigurationValue(UNKNOWN_KEY));
	}
	
	@Test
	public void testGetNull() {
		assertNull(bean.getConfigurationValue(null));
		
		
	}
}
