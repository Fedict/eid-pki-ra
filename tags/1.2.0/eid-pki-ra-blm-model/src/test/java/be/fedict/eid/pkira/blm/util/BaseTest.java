/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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
