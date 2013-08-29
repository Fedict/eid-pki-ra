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
public class CertificateAuthorityEditSeleniumTest extends BaseSeleniumTestCase {
	
	private static final String CANEWNAME = "SeleniumTest Name Edit";
	private static final String CANEWURL = "http://SeleniumTestUrl.be";
	private static final String CANEWLEGALNOTICE = "The new legal notice";
	
	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	@Test
	public void testListContent() {
		goToEditFirstCertificateAuthority();
		assertTextPresent("Edit certificate authority");
	}

	@Test
	public void updateCASystemName(){		
		goToEditFirstCertificateAuthority();
		assertTextPresent("CA system name:");
		getSelenium().type("certificateDetailForm:casystemname:nameEdit", CANEWNAME);
		clickAndWait("certificateDetailForm:submitButtonBox:update");
		
		goToEditFirstCertificateAuthority();
		
		assertTextPresent("CA system name:");
		Assert.assertEquals(CANEWNAME, getSelenium().getValue("certificateDetailForm:casystemname:nameEdit"));	
	}
	
	@Test(dependsOnMethods="updateCASystemName")
	public void updateCASystemNameButCancel(){
		goToEditFirstCertificateAuthority();
		assertTextPresent("CA system name:");
		getSelenium().type("certificateDetailForm:casystemname:nameEdit", "SecondSeleniumTestName");
		clickAndWait("certificateDetailForm:submitButtonBox:back");
		
		goToEditFirstCertificateAuthority();
		
		assertTextPresent("CA system name:");
		Assert.assertEquals(CANEWNAME, getSelenium().getValue("certificateDetailForm:casystemname:nameEdit"));	
	}

	@Test
	public void updateURL(){		
		goToEditFirstCertificateAuthority();
		assertTextPresent("URL:");
		getSelenium().type("certificateDetailForm:url:nameEdit", CANEWURL);
		clickAndWait("certificateDetailForm:submitButtonBox:update");
		
		goToEditFirstCertificateAuthority();
		
		assertTextPresent("URL:");
		Assert.assertEquals(CANEWURL, getSelenium().getValue("certificateDetailForm:url:nameEdit"));	
	}
	
	@Test(dependsOnMethods="updateURL")
	public void updateURLButCancel(){
		goToEditFirstCertificateAuthority();
		assertTextPresent("URL:");
		getSelenium().type("certificateDetailForm:url:nameEdit", "SecondSeleniumTestUrl");
		clickAndWait("certificateDetailForm:submitButtonBox:back");
		
		goToEditFirstCertificateAuthority();
		
		assertTextPresent("URL:");
		Assert.assertEquals(CANEWURL, getSelenium().getValue("certificateDetailForm:url:nameEdit"));	
	}

	@Test
	public void updateLegalNotice(){		
		goToEditFirstCertificateAuthority();
		assertTextPresent("Legal Notice:");
		getSelenium().type("certificateDetailForm:legalnotice:csr", CANEWLEGALNOTICE);
		clickAndWait("certificateDetailForm:submitButtonBox:update");
		
		goToEditFirstCertificateAuthority();
		
		assertTextPresent("Legal Notice:");
		Assert.assertEquals(CANEWLEGALNOTICE, getSelenium().getValue("certificateDetailForm:legalnotice:csr"));	
	}
	
	@Test(dependsOnMethods="updateLegalNotice")
	public void updateLegalNoticeButCancel(){
		goToEditFirstCertificateAuthority();
		assertTextPresent("Legal Notice:");
		getSelenium().type("certificateDetailForm:legalnotice:csr", "Logel Notice Selenium Test");
		clickAndWait("certificateDetailForm:submitButtonBox:back");
		
		goToEditFirstCertificateAuthority();
		
		assertTextPresent("Legal Notice:");
		Assert.assertEquals(CANEWLEGALNOTICE, getSelenium().getValue("certificateDetailForm:legalnotice:csr"));	
	}
	
	@Test
	public void addNewParameterItem(){
		goToEditFirstCertificateAuthority();
		
		getSelenium().type("certificateDetailForm:addparameter:paraKey", "TheKey");
		getSelenium().type("certificateDetailForm:addparameter:paraValue", "TheValue");
		
		clickAndWait("certificateDetailForm:addparameter:add");
	
		Number actual = getSelenium().getXpathCount("//table[@id='certificateDetailForm:parameters:parametersTable']/tbody/tr");
		Assert.assertEquals(1, actual);
	}

	@Test(dependsOnMethods="addNewParameterItem")
	public void deleteParameterItem(){
		goToEditFirstCertificateAuthority();
		
		clickAndWait("certificateDetailForm:parameters:parametersTable:0:delete");
		getSelenium().getConfirmation();
		getSelenium().chooseOkOnNextConfirmation();
		
		Number actual = getSelenium().getXpathCount("//table[@id='certificateDetailForm:parameters:parametersTable']/tbody/tr");
		Assert.assertEquals(0, actual);
	}

	private void goToEditFirstCertificateAuthority() {
		clickAndWait("header-form:certificateauthorities");		
		clickAndWait("certificateAuthorityListForm:certificateAuthorityTable:0:edit");		
	}
}
