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

import org.apache.commons.lang.BooleanUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.integration.BaseSeleniumTestCase;

/**
 * @author Bram Baeyens
 *
 */
public class CertificateDomainUpdateSeleniumTest extends BaseSeleniumTestCase {
	
	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	@Test
	public void testUpdateCertificateDomain() {
		updateCertificateDomain(0, null, "c=be,ou=test5,uid=*", true, false, true);
		assertTextPresent("The certificate domain has been updated.");
	}

	@Test
	public void testUpdateCertificateDomainInvalidExpression() {
		updateDnExpression(0, "xyz");
		assertTextPresent("The distinguished name expression is not valid");
		getSelenium().type("certificateDetailForm:dnPatternDecoration:dnPattern", "c=au,ou=eHealth,uid=*");
		clickAndWait("certificateDetailForm:submitButtonBox:update");
		assertTextPresent("The certificate domain has been updated.");
	}
	
	@Test
	public void testUpdateCertificateDomainNothingChecked() {
		updateCertificateTypes(0, false, false, false);
		assertTextPresent("Please select at least one certificate type for this certificate domain");
		getSelenium().check("certificateDetailForm:clientCertDecoration:clientCert");
		getSelenium().check("certificateDetailForm:serverCertDecoration:serverCert");
		getSelenium().uncheck("certificateDetailForm:codeSigningCertDecoration:codeSigningCert");
		clickAndWait("certificateDetailForm:submitButtonBox:update");
		assertTextPresent("The certificate domain has been updated.");
	}

	@Test
	public void testUpdateCertificateDomainOverlapping() {
		updateDnExpression(0, "c=be,ou=eHealth,uid=*");
		assertTextPresent("The distinguished name expression overlaps with an existing certificate domain");
		getSelenium().type("certificateDetailForm:dnPatternDecoration:dnPattern", "c=de,ou=eHealth,uid=*");
		clickAndWait("certificateDetailForm:submitButtonBox:update");
		assertTextPresent("The certificate domain has been updated.");
	}

	@Test
	public void testUpdateCertificateDomainDnNull() {
		updateDnExpression(0, "");
		assertTextPresent("This field is required!");
		getSelenium().type("certificateDetailForm:dnPatternDecoration:dnPattern", "c=en,ou=eHealth,uid=*");
		clickAndWait("certificateDetailForm:submitButtonBox:update");
		assertTextPresent("The certificate domain has been updated.");
	}
	
	private void updateDnExpression(int rowToUpdate, String dnExpr) {
		updateCertificateDomain(rowToUpdate, null, dnExpr, null, null, null);
	}
	
	private void updateCertificateTypes(int rowToUpdate, Boolean clientCert, Boolean serverCert, Boolean codeSigningCert) {
		updateCertificateDomain(rowToUpdate, null, null, clientCert, serverCert, codeSigningCert);
	}	
	
	private void updateCertificateDomain(int rowToUpdate, String name, String dnExpr, Boolean clientCert, Boolean serverCert,
			Boolean codeSigningCert) {
		clickAndWait("header-form:certificatedomains");
		clickAndWait(deriveEditLinkToClick(rowToUpdate));
		assertTextPresent("Edit certificate domain");

		if (name != null) {
			getSelenium().type("certificateDetailForm:nameDecoration:name", name);
		}
		if (dnExpr != null) {
			getSelenium().type("certificateDetailForm:dnPatternDecoration:dnPattern", dnExpr);
		}
		if (BooleanUtils.isTrue(clientCert)) {
			getSelenium().check("certificateDetailForm:clientCertDecoration:clientCert");
		} else if (BooleanUtils.isFalse(clientCert)) {
			getSelenium().uncheck("certificateDetailForm:clientCertDecoration:clientCert");
		}
		if (BooleanUtils.isTrue(serverCert)) {
			getSelenium().check("certificateDetailForm:serverCertDecoration:serverCert");
		} else if (BooleanUtils.isFalse(serverCert)) {
			getSelenium().uncheck("certificateDetailForm:serverCertDecoration:serverCert");
		}
		if (BooleanUtils.isTrue(codeSigningCert)) {
			getSelenium().check("certificateDetailForm:codeSigningCertDecoration:codeSigningCert");
		} else if (BooleanUtils.isFalse(codeSigningCert)) {
			getSelenium().uncheck("certificateDetailForm:codeSigningCertDecoration:codeSigningCert");
		}
		clickAndWait("certificateDetailForm:submitButtonBox:update");
	}

	private String deriveEditLinkToClick(int rowToUpdate) {
		return "certificateDomainListForm:certificateDomainTable:" + rowToUpdate + ":edit";
	}
}
