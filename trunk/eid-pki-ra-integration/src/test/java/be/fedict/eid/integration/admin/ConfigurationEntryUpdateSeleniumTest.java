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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.integration.BaseSeleniumTestCase;

/**
 * @author Bram Baeyens
 *
 */
public class ConfigurationEntryUpdateSeleniumTest extends BaseSeleniumTestCase {

	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	@Test
	public void testUpdateConfigurationEntry() {
		updateConfigurationEntry(2004, "100,150,300");
		assertTextPresent("The configuration parameter has been updated.");
		assertTextPresent("100,150,300");
	}

	@Test
	public void testUpdateConfigurationEntryInvalidValue() {
		updateConfigurationEntry(2004, "abc");
		assertTextPresent("Please add a valid value for the configuration entry");
	}

	@Test
	public void testUpdateConfigurationEntryEmptyValue() {
		updateConfigurationEntry(2004, "");
		assertTextPresent("Please add a valid value for the configuration entry");
	}
	
	private void updateConfigurationEntry(int entryId, String value) {
		clickAndWait("header-form:configurationentries");
		clickAndWait(deriveEditLinkToClick(entryId));
		assertTextPresent("Edit the configuration parameter");

		if (value != null) {
			getSelenium().type("configurationEntryForm:valueDecoration:configurationEntryValue", value);
		}
		clickAndWait("configurationEntryForm:submitButtonBox:update");
	}

	private String deriveEditLinkToClick(int entryId) {
		return "//a[starts-with(@href, '/eid-pki-ra/admin/page/configurationentry/configurationentry-edit.seam?configurationEntryId=" + entryId + "')]";
	}
}
