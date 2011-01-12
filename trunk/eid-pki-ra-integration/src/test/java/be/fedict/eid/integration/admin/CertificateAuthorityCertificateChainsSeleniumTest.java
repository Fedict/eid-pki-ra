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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.integration.BaseSeleniumTestCase;

/**
 * @author Bram Baeyens
 */
public class CertificateAuthorityCertificateChainsSeleniumTest extends BaseSeleniumTestCase {

	private static final String CLIENTCA_SUBJECT = "C=BE,CN=Citizen CA,SERIALNUMBER=201007";
	private static final String SERIAL_CACERT1 = "117029288888937864350596520176844645968";
	private static final String SERIAL_CACERT2 = "3098404661496965511";
	private static final String SERIAL_CLIENTCA = "51037179132502879506713585292514906207";

	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	private void goToChainsPage() {
		clickAndWait("header-form:certificateauthorities");
		clickAndWait("certificateAuthorityListForm:certificateAuthorityTable:0:chains");
	}

	@Test
	public void uploadCertificate() {
		goToChainsPage();

		assertTextPresent("No certificates");

		uploadCertificate("rootca1.crt");
		assertTextNotPresent("No certificates");
		assertTextPresent(SERIAL_CACERT1);
	}

	@Test(dependsOnMethods = "uploadCertificate")
	public void uploadCertificateAgain() {
		assertTextPresent(SERIAL_CACERT1);

		// Upload the same certificate again
		goToChainsPage();
		uploadCertificate("rootca1.crt");
		assertTextPresent("already present");
	}

	@Test(dependsOnMethods = "uploadCertificate")
	public void uploadCertificateWithChild() {
		goToChainsPage();

		// Try without parent
		uploadCertificate("childca.crt");
		assertTextPresent("issuer of the certificate could not be found");
		assertTextNotPresent(SERIAL_CLIENTCA);

		// Try with parent account
		uploadCertificate("rootca2.crt");
		assertTextPresent(SERIAL_CACERT2);
		uploadCertificate("childca.crt");
		assertTextPresent(SERIAL_CLIENTCA);
	}

	@Test(dependsOnMethods = "uploadCertificateAgain")
	public void deleteCertificate() {
		assertTextPresent(SERIAL_CACERT1);

		// Click the delete button
		clickAndWait("certificateListForm:certificateTable:0:delete");
		assertTextNotPresent(SERIAL_CACERT1);
	}

	@Test(dependsOnMethods =
		{ "uploadCertificateWithChild", "deleteCertificate" })
	public void assignCertificates() {
		goToChainsPage();
		assertTextPresent(SERIAL_CLIENTCA);

		getSelenium().select("certificateChainForm:clientCertificateChainDecoration:j_id46",
				"label=" + CLIENTCA_SUBJECT);
		getSelenium().select("certificateChainForm:serverCertificateChainDecoration:j_id59",
				"label=" + CLIENTCA_SUBJECT);
		getSelenium().select("certificateChainForm:codeSigningCertificateChainDecoration:j_id72",
				"label=" + CLIENTCA_SUBJECT);
		clickAndWait("certificateChainForm:submitButtonBox:update");

		goToChainsPage();
		assertEquals("1", getValue("certificateChainForm:clientCertificateChainDecoration:j_id46"));
		assertEquals("1", getValue("certificateChainForm:serverCertificateChainDecoration:j_id59"));
		assertEquals("1", getValue("certificateChainForm:codeSigningCertificateChainDecoration:j_id72"));
	}
	
	@Test(dependsOnMethods="assignCertificates")
	public void deleteAssignedCertificate() {
		goToChainsPage();
		assertTextPresent(SERIAL_CACERT2);
		
		// Click the delete button
		clickAndWait("certificateListForm:certificateTable:0:delete");
		assertTextPresent("No certificates");
		assertFalse ("1".equals(getValue("certificateChainForm:clientCertificateChainDecoration:j_id46")));
		assertFalse ("1".equals(getValue("certificateChainForm:serverCertificateChainDecoration:j_id59")));
		assertFalse ("1".equals(getValue("certificateChainForm:codeSigningCertificateChainDecoration:j_id72")));
	}

	private String getCertificateDirectory() {
		return new File(".").getAbsolutePath() + "/src/test/resources/crt";
	}

	private void uploadCertificate(String certificateFile) {
		type("certificateUploadForm:certificateUploadDecoration:certificateUpload", getCertificateDirectory() + "/"
				+ certificateFile);
		click("certificateUploadForm:certificateUploadDecoration:upload");
		waitForPageToLoad();
	}
}
