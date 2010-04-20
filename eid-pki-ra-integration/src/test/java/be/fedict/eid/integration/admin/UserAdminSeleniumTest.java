/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

package be.fedict.eid.integration.admin;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.integration.BaseSeleniumTestCase;

/**
 * @author Bram Baeyens
 *
 */
public class UserAdminSeleniumTest extends BaseSeleniumTestCase {
	
	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	@Test
	public void testRevokeAdmin() {
		executeTest(0, true, false, 0);
	}

	@Test
	public void testGrantAdmin() {
		executeTest(1, false, false, 0);
	}

	@Test
	public void testCurrentUser() {
		executeTest(2, true, true, 0);
	}	
	
	private void executeTest(int rowToUpdate, boolean isAdmin, boolean isCurrentUser, int numberOfRegistrations) {
		clickAndWait("header-form:users");
		clickAndWait(deriveEditLinkToClick(rowToUpdate));
		assertTextPresent("User");
		assertTextPresent("Registered certificate domains");
		if (numberOfRegistrations == 1) {
			assertTextPresent("There are no registered certificate domains for this user");
		} else {
			validateRows(numberOfRegistrations);
		}
		if (isAdmin && !isCurrentUser) {
			clickAndWait("userForm:submitButtonBox:revokeAdmin");
			assertTextPresent("The user has been updated.");
			clickAndWait(deriveEditLinkToClick(rowToUpdate));
			clickAndWait("userForm:submitButtonBox:grantAdmin");
		}
		if (!isAdmin && !isCurrentUser) {
			clickAndWait("userForm:submitButtonBox:grantAdmin");
			assertTextPresent("The user has been updated.");
			clickAndWait(deriveEditLinkToClick(rowToUpdate));
			clickAndWait("userForm:submitButtonBox:revokeAdmin");
		}
		if (isCurrentUser) {
			assertTextNotPresent("Revoke admin rights");
			assertTextNotPresent("Grant admin rights");
		}
	}

	private String deriveEditLinkToClick(int rowToUpdate) {
		return "userListForm:allUser:" + rowToUpdate + ":edit";
	}

	private void validateRows(Integer numberOfRows) {
		Number actual = getSelenium().getXpathCount("//table[@id='userForm:userRegistrations']/tbody/tr");
		Assert.assertEquals(numberOfRows + " rows expected, but found " + actual, numberOfRows, actual);
	}
}
