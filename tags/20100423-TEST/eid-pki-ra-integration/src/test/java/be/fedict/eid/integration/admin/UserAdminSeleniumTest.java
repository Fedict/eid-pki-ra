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
import org.testng.annotations.BeforeMethod;
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
	
	@BeforeMethod
	public void goToUserList() {
		clickAndWait("header-form:users");
	}

	@Test
	public void testRevokeAdmin() {
		goToUserEditPage(0);
		validateRegistrationRows(2);
		Assert.assertTrue(getSelenium().getAllButtons().length == 2);
		clickAndWait("userForm:submitButtonBox:revokeAdmin");
		assertTextPresent("The user has been updated.");
	}

	@Test
	public void testGrantAdmin() {
		goToUserEditPage(1);
		validateRegistrationRows(0);
		Assert.assertTrue(getSelenium().getAllButtons().length == 2);
		clickAndWait("userForm:submitButtonBox:grantAdmin");
		assertTextPresent("The user has been updated.");
	}

	@Test
	public void testCurrentUser() {
		goToUserEditPage(2);
		validateRegistrationRows(1);
		Assert.assertTrue(getSelenium().getAllButtons().length == 1);
	}	
	
	@Test
	public void testRemoveRegistrationCancel() {
		goToUserEditPage(0);	
		getSelenium().chooseCancelOnNextConfirmation();
		click(deriveDeleteLinkToClick(0));
		Assert.assertTrue(getSelenium().isConfirmationPresent());
		Assert.assertEquals("Are you sure you want to delete the registration", getSelenium().getConfirmation());
		validateRegistrationRows(2);
	}

	@Test
	public void testRemoveRegistrationOk() {
		goToUserEditPage(2);
		click(deriveDeleteLinkToClick(0));
		Assert.assertTrue(getSelenium().isConfirmationPresent());
		Assert.assertEquals("Are you sure you want to delete the registration", getSelenium().getConfirmation());
		waitForPageToLoad();
		assertTextPresent("The registered certificate domain has been removed");
		validateRegistrationRows(0);
	}
	
	private void goToUserEditPage(int rowToEdit) {		
		clickAndWait(deriveEditLinkToClick(rowToEdit));
		assertTextPresent("User");
		assertTextPresent("Registered certificate domains");		
	}

	private String deriveEditLinkToClick(int rowToEdit) {
		return "userListForm:allUser:" + rowToEdit + ":edit";
	}

	private String deriveDeleteLinkToClick(int rowToDelete) {
		return "userForm:userRegistrations:" + rowToDelete + ":delete";
	}

	private void validateRegistrationRows(Integer expectedRows) {
		if (expectedRows == 0) {
			assertTextPresent("There are no registered certificate domains for this user");
		} else {
			Number actual = getSelenium().getXpathCount("//table[@id='userForm:userRegistrations']/tbody/tr");
			Assert.assertEquals(expectedRows + " rows expected, but found " + actual, expectedRows, actual);
		}
	}
}
