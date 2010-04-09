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
package be.fedict.eid.integration;

import java.sql.SQLException;

import org.junit.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import be.fedict.eid.integration.util.ConnectionDetailsReader;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Base test case from which all selenium test cases will extend.
 * 
 * Requires the following system properties:
 * - <i>webidm.test.selenium.server</i> - The host where the selenium server runs
 * - <i>webidm.test.selenium.port</i> - The port of the selenium server
 * - <i>webidm.test.selenium.url</i> - The url of the webapplication to test
 * - <i>webidm.test.selenium.browser</i> - The browser to be used
 * - <i>webidm.test.selenium.pageLoadTime</i> - The timeout time when waiting for a page to load
 * - <i>webidm.test.selenium.stopBrowser</i> - Should the browser be closed when the test has finished
 * - <i>webidm.test.selenium.pauseSpeed</i> - Pause speed between selenium commands
 * 
 * @author 
 *
 */
public class BaseSeleniumTestCase {
	
	public static final String DEFAULT_PAGE_LOADTIME = "5000";
	
	private static String deployUrl;	
	private static Selenium selenium;

	@BeforeSuite
	@Parameters({"context"})
	public static Selenium setUp(String context) throws SQLException, ClassNotFoundException {
		startSeleniumServer(context);
        selenium.allowNativeXpath("false");
        return selenium;
	}	
	
	/**
     * Stop the selenium server.
     */
	@AfterSuite
    public static void tearDown() throws SQLException {
    	stopSeleniumServer();
    }
	
	private static void startSeleniumServer(String context) {
		String server = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.selenium.server");
        int port = Integer.valueOf(ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.selenium.port"));
        deployUrl = getApplicationBaseUrl(context);
		String browser = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.selenium.browser");
        selenium = new DefaultSelenium(server, port, browser, deployUrl);
        
        String pauseSpeed = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.selenium.pauseSpeed");
        if(pauseSpeed != null && !pauseSpeed.equals("")) {
        	selenium.setSpeed(pauseSpeed);
        }
        
        selenium.start();
	}	
	
	private static void stopSeleniumServer() {
		String stopBrowser = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.selenium.stopBrowser");
    	Boolean stopBr = Boolean.valueOf(stopBrowser);
    	if(stopBr) {
    		getSelenium().stop();
    	}
	}
	
	private static String getApplicationBaseUrl(String context) {
		return ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.selenium.url." + context);
	}
    
    protected static String getDeployURL() {
		return deployUrl;
	}
	
	protected static Selenium getSelenium() {
		return selenium;
	}
	
	protected void assertDropdownContainsNumberOfOptions(String locator, int numberOfOptionsExpected) {
		int actualNumberOfOptions = getSelenium().getSelectOptions(locator).length;
		Assert.assertTrue("Expected '" + numberOfOptionsExpected + "' options in dropdown, but found '" + actualNumberOfOptions + "'.", actualNumberOfOptions == numberOfOptionsExpected);
	}
    
	protected void assertElementNotPresent(String locator) {
		Assert.assertTrue("Element \"" + locator + "\": Should not be present.", isElementPresent(locator) == false) ;
	}
	
    protected void assertElementPresent(String locator) {
		Assert.assertTrue( "Element \"" + locator + "\": Should be present.", isElementPresent(locator) == true) ;
	}

	protected void assertElementPresentInDropDown(String locator, String element) {
		String[] options = getSelenium().getSelectOptions(locator);

		boolean found = false;

		for (int i = 0; i < options.length; i++) {
			if (options[i].equals(element)) {
				found = true;
			}
		}
		Assert.assertTrue("Element: \"" + element + "\" not found in dropdown \"" + locator + ".", found);
	}

	protected void assertIsEditable(String locator) {
		Assert.assertTrue("Expected that '" + locator + "' was editable, but was not editable", isEditable(locator));
	}


	protected void assertIsNotEditable(String locator) {
		Assert.assertTrue("Expected that '" + locator + "' was not editable, but was editable", !isEditable(locator));
	}


	protected void assertTextNotPresent(String text) {
		Assert.assertTrue( "\"" + text + "\": Should not be present.", getSelenium().isTextPresent(text) == false) ;
	}
	
	protected void assertTextPresent(String text) {
		Assert.assertTrue( "\"" + text + "\": Should be present.", getSelenium().isTextPresent(text)) ;
	}
	
	protected void assertValueOfElementPresent(String locator, String expectedValue) {
		String actualValue = getSelenium().getValue(locator);
		Assert.assertTrue("Expected value '" + expectedValue + "', but was actual '" + actualValue + "'.", expectedValue.equals(actualValue));
	}
	
	protected void autoLogin() {
		// This assumes that a user with RRN 99999999999 is in the database and
		// the application is running in debug mode.
		openAndWait(getDeployURL() + "postLogin.seam");
		
		assertTextPresent("Log out");
	}
	
	protected void click(String locator) {
		getSelenium().click(locator);
	}

	protected void clickAndWait(String locator) {
		getSelenium().click(locator);
		waitForPageToLoad();
	}
	
	protected String getText(String locator) {
		return getSelenium().getText(locator);
	}
	
	protected String getValue(String locator) {
		return getSelenium().getValue(locator);
	}
	
	protected boolean isEditable(String locator) {
		return getSelenium().isEditable(locator);
	}
	
	protected boolean isElementPresent(String locator) {
		return getSelenium().isElementPresent(locator);
	}
	
	protected boolean isTextPresent(String text) {
		return getSelenium().isTextPresent(text);
	}
	
	protected void openAndWait(String url) {
		getSelenium().open(url);		
		waitForPageToLoad();
	}
	
	protected void type(String locator, String text) {
		getSelenium().type(locator, text);
	}
	
    protected void waitForPageToLoad() {
    	String pageLoadTime = ConnectionDetailsReader.getConnectionProperty("be.fedict.eid.integration.selenium.pageLoadTime");
    	if(pageLoadTime == null) {
    		pageLoadTime = DEFAULT_PAGE_LOADTIME;
    	}

    	long time = System.currentTimeMillis();
        getSelenium().waitForPageToLoad(pageLoadTime);
        long time2 = System.currentTimeMillis() - time;
        System.out.println(time2);
    }
}