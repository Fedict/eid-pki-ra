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

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.integration.BaseSeleniumTestCase;

/**
 * @author Bram Baeyens
 *
 */
public class ContractListSeleniumTest extends BaseSeleniumTestCase {
	
	private static final String DROP_DOWN_LOCATOR = "contractListForm:certificateDomainDecorator:selectRegisteredCertificateDomain";
	
	@BeforeClass
	public void login() {
		super.autoLogin();
	}
	
	@Test
	public void testContractListArrive() {
		goToContractOverview();
		validateContractListPage("org.jboss.seam.ui.NoSelectionConverter.noSelectionValue", "", 6);		
	}
	
	@Test
	public void testContractListCD1001Selected() {
		goToContractOverview();
		selectCertificateDomain("eHealth Client Certificates", "c=be,ou=eHealth,uid=*");
		validateContractListPage("1001", "c=be,ou=eHealth,uid=*", 0);
	}
	
	@Test
	public void testContractListCD1002Selected() {
		goToContractOverview();
		selectCertificateDomain("eHealth Server Certificates", "c=be,ou=eHealth,cn=*");
		validateContractListPage("1002", "c=be,ou=eHealth,cn=*", 3);
	}
	
	@Test
	public void testContractListCD1003Selected() {
		goToContractOverview();
		selectCertificateDomain("eHealth Code Signing Certificates", "c=be,ou=eHealth,uid=*");
		validateContractListPage("1003", "c=be,ou=eHealth,uid=*", 3);
	}
	
	@Test
	public void testContractListCD1004Selected() {
		goToContractOverview();
		selectCertificateDomain("Test", "O=ACA,C=BE");
		validateContractListPage("1004", "O=ACA,C=BE", 0);
	}

	private void selectCertificateDomain(String certificateDomainId, String expectedDnExpression) {
		StringBuilder js = new StringBuilder()
			.append("selenium.browserbot.getCurrentWindow()")
			.append(".document.getElementById('contractListForm:dnExpressionDecorator:certificateDomainDnExpression')")
			.append(".innerHTML == '")
			.append(expectedDnExpression)
			.append("'");
		getSelenium().select(DROP_DOWN_LOCATOR, certificateDomainId);
		getSelenium().waitForCondition(js.toString(), "5000");
	}

	private void goToContractOverview() {
		clickAndWait("header-form:contracts");
	}

	private void validateContractListPage(String expectedSelectedId, String expectedDnExpression, Integer expectedRows) {
		checkSelectedCertificateDomain(expectedSelectedId);
		checkDnExpression(expectedDnExpression);
		validateRows(expectedRows);
	}
	
	private void checkSelectedCertificateDomain(String expectedSelectedId) {
		String actualSelectedId = getSelenium().getSelectedValue(DROP_DOWN_LOCATOR);
		Assert.assertEquals(expectedSelectedId, actualSelectedId);		
	}

	private void checkDnExpression(String expectedDnExpression) {
		String actualDnExpression = getSelenium().getText("contractListForm:dnExpressionDecorator:certificateDomainDnExpression");
		Assert.assertEquals(expectedDnExpression, actualDnExpression);		
	}

	private void validateRows(Integer expectedRows) {
		Number actualRows = getSelenium().getXpathCount("//table[@id='contractListForm:contractTable']/tbody/tr");
		Assert.assertEquals(expectedRows, actualRows);
	}
}
