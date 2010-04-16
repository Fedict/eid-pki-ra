package be.fedict.eid.integration.admin;

import be.fedict.eid.integration.BaseSeleniumTestCase;

public class DetailRegistrationSeleniumTest extends BaseSeleniumTestCase {
	
	private String pageOverviewTitle = "Handle registrations"; 
	private String pageDetailTitle = "Approval Detail overview"; 	
	
	private void createCertificateDomain() {
		openAndWait(getDeployURL());
		clickAndWait("header-form:registrations");
		assertTextPresent(pageOverviewTitle);
		assertTextPresent("eHealth Client Certificates");
		
		clickAndWait("bulkApprovalForm:detailItem");
		assertTextPresent(pageDetailTitle);
		assertDropdownContainsNumberOfOptions("approvalDetailForm:cdDropdown", 2);
	}
}
