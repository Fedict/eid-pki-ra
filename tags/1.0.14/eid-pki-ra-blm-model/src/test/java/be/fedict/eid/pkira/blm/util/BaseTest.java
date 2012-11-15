package be.fedict.eid.pkira.blm.util;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.util.ComponentFactory;

public class BaseTest  extends DatabaseTest{ 
	@BeforeMethod
	public void setTestComponentFactory() {
		ComponentFactory.setCurrentComponentFactory(new TestComponentFactory());
	}
	
	@AfterMethod
	public void resetMockContext() {
		MockContext.getInstance().reset();
	}
	
	@AfterMethod
	public void resetComponentFactory() {
		ComponentFactory.reset();
	}
	
	public void registerMock(String name, Object object) {
		MockContext.getInstance().registerMock(name, object);
	}
}
