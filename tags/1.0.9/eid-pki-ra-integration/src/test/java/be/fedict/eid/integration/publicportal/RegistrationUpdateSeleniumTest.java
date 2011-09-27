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

package be.fedict.eid.integration.publicportal;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.integration.BaseSeleniumTestCase;

/**
 * @author Bram Baeyens
 *
 */
public class RegistrationUpdateSeleniumTest extends BaseSeleniumTestCase {
	
	@BeforeClass
	public void login() {
		super.autoLogin();
	}
	
	@Test
	public void updateRegistration() {
		updateRegistrationEmail("pietje.puk@aca-it.be", "Your registration has been updated.");
	}
	
	@Test
	public void updateRegistrationInvalidEmail() {
		updateRegistrationEmail("invalid", "The e-mail address is invalid.");
	}
	
	@Test
	public void updateRegistrationNoEmail() {
		updateRegistrationEmail("", "This field is required!");
	}
	
	private void updateRegistrationEmail(String newEmail, String expectedText) {
		clickAndWait("header-form:registrations");
		clickAndWait("registrationListForm:registrationsTable:0:edit");
		getSelenium().type("registrationForm:emailDecoration:email", newEmail);
		clickAndWait("registrationForm:submitButtonBox:createOrUpdate");
		assertTextPresent(expectedText);
	}
}
