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
public class ReportGenerationSeleniumTest extends BaseSeleniumTestCase {

	@BeforeClass
	public void login() {
		super.autoLogin();
	}

	@Test
	public void testGenerateReport() {
		setReportParameters("2010-04", "2010-05");
		//click("reportConfigurationForm:submitButtonBox:generateReport"); // Cannot be tested: download of a file is not supported		
	}
	
	@Test
	public void testGenerateReportMonthsInvalid() {
		setReportParameters("2010-05", "2010-04");
		clickAndWait("reportConfigurationForm:submitButtonBox:generateReport");	
		assertTextPresent("The from month cannot be after the end month.");
	}

	private void setReportParameters(String startMonth, String endMonth) {
		clickAndWait("header-form:reports");
		getSelenium().select("reportConfigurationForm:startMonthDecoration:startMonth", startMonth);
		getSelenium().select("reportConfigurationForm:endMonthDecoration:endMonth", endMonth);
		getSelenium().uncheck("reportConfigurationForm:includeCAReportDecoration:includeCAReport");
	}
}
