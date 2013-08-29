/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

package be.fedict.eid.pkira.blm.model.usermgmt;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import javax.persistence.PersistenceException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;

/**
 * @author Bram Baeyens
 *
 */
public class UserRepositoryBeanTest extends DatabaseTest {
	
	private static final String NRN = "uTestNRN";
	private static final String FIRST_NAME = "uTestFN";
	private static final String LAST_NAME = "uTestLN";
	
	private UserRepositoryBean repository;
	private User valid;
	
	@BeforeClass
	public void setup() {
		repository = new UserRepositoryBean();
		repository.setEntityManager(getEntityManager());
		valid = createUser(NRN, FIRST_NAME, LAST_NAME);
	}

	@Test
	public void persist() throws Exception {		
		repository.persist(valid);
		forceCommit();
		assertNotNull(valid.getId());
	}
	
	@Test(dependsOnMethods="persist", expectedExceptions=PersistenceException.class)
	public void sameNationalRegisterNumber() throws Exception {
		repository.persist(createUser(NRN, FIRST_NAME, LAST_NAME));
		fail("PersistenceException expected");
	}
	
	@Test(dependsOnMethods="persist")
	public void findByNationalRegisterNumber() throws Exception {
		User user = repository.findByNationalRegisterNumber(NRN);
		assertEquals(user, valid);
	}	
	
	@Test(dependsOnMethods="persist")
	public void getUserCount() {
		assertFalse(0 == repository.getUserCount());
	}
	
	private User createUser(String nationalRegisterNumber, String firstName, String lastName) {
		User user = new User();
		user.setNationalRegisterNumber(nationalRegisterNumber);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setLocale("en");
		return user;
	}
}
