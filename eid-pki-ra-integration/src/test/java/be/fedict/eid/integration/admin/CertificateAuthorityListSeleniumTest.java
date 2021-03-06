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
public class CertificateAuthorityListSeleniumTest extends BaseSeleniumTestCase {
	
	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	@Test
	public void testListContent() {
		goToCertificateAuthorityList();
		validateRows(1);
	}

	private void validateRows(Integer numberOfRows) {
		Number actual = getSelenium().getXpathCount("//table[@id='certificateAuthorityListForm:certificateAuthorityTable']/tbody/tr");
		Assert.assertEquals(numberOfRows + " rows expected, but found " + actual, numberOfRows, actual);
	}

	private void goToCertificateAuthorityList() {
		clickAndWait("header-form:certificateauthorities");		
	}
}
