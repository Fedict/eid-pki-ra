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
package be.fedict.eid.integration.admin;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.integration.BaseSeleniumTestCase;

/**
 * @author Jan Van den Bergh
 */
public class CertificateDomainCreateSeleniumTest extends BaseSeleniumTestCase {

	private static final String DNEXPR_INVALID = "xyz";
	private static final String DNEXPR1 = "c=be,ou=test,cn=*";
	private static final String DNEXPR2 = "c=be,ou=test2,cn=*";
	private static final String NAME1 = "First Domain";
	private static final String NAME2 = "Second Domain";
	private static final String NAME3 = "Third Domain";

	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	@Test
	public void testCreateFirstCertificateDomain() {
		createCertificateDomain(NAME1, DNEXPR1, true, false, false, false);
		assertTextPresent("The certificate domain has been added to the database");
	}
	
	@Test
	public void testCreateCertificateDomainInvalidExpression() {
		createCertificateDomain(NAME3, DNEXPR_INVALID, true, false, false, false);
		assertTextPresent("The distinguished name expression is not valid");
	}
	
	@Test
	public void testCreateCertificateDomainNothingChecked() {
		createCertificateDomain(NAME3, DNEXPR2, false, false, false, false);
		assertTextPresent("Please select at least one certificate type for this certificate domain");
	}

	@Test(dependsOnMethods = "testCreateFirstCertificateDomain")
	public void testCreateCertificateDomainOverlapping() {
		createCertificateDomain(NAME3, DNEXPR1, true, true, true, true);
		assertTextPresent("The distinguished name expression overlaps with an existing certificate domain");
	}

	@Test
	public void testCreateCertificateDomainNameNull() {
		createCertificateDomain("", DNEXPR1, true, true, true, true);
		assertTextPresent("This field is required!");
	}

	@Test
	public void testCreateCertificateDomainDnNull() {
		createCertificateDomain(NAME3, "", true, true, true, true);
		assertTextPresent("This field is required!");
	}

	@Test(dependsOnMethods = "testCreateFirstCertificateDomain")
	public void testCreateCertificateDomainSameName() {
		createCertificateDomain(NAME1, DNEXPR2, true, false, false, false);
		assertTextPresent("The name of the certificate domain already exists");
	}

	@Test(dependsOnMethods = "testCreateFirstCertificateDomain")
	public void testCreateSecondDomain() {
		createCertificateDomain(NAME2, DNEXPR1, false, true, true, true);
		assertTextPresent("The certificate domain has been added to the database");
	}

	private void createCertificateDomain(String name, String dnExpr, boolean clientCert, boolean serverCert,
			boolean codeSigningCert, boolean personsCert) {
		clickAndWait("header-form:certificatedomains");
		clickAndWait("certificateDomainListForm:submitButtonBox:newCertificateDomain");
		assertTextPresent("Create certificate domain");

		getSelenium().type("certificateDetailForm:nameDecoration:nameEdit", name);
		getSelenium().type("certificateDetailForm:dnPatternDecoration:dnPattern", dnExpr);
		if (clientCert) {
			getSelenium().check("certificateDetailForm:clientCertDecoration:clientCert");
		}
		if (serverCert) {
			getSelenium().check("certificateDetailForm:serverCertDecoration:serverCert");
		}
		if (codeSigningCert) {
			getSelenium().check("certificateDetailForm:codeSigningCertDecoration:codeSigningCert");
		}
		if (personsCert) {
			getSelenium().check("certificateDetailForm:personsCertDecoration:personsCert");
		}
		clickAndWait("certificateDetailForm:submitButtonBox:save");
	}	
}
