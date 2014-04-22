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
package be.fedict.eid.pkira.blm.errorhandling;

import org.jboss.seam.log.Logging;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ErrorLoggerBeanTest {

	private ErrorLoggerBean bean;

	@BeforeMethod
	public void setup() {
		bean = new ErrorLoggerBean();
		bean.setLog(Logging.getLog(ErrorLoggerBean.CATEGORY));
	}
	
	@Test
	public void testLogging() {
		bean.logError(ApplicationComponent.MAIL, "Mail error", new RuntimeException());
		bean.logError(ApplicationComponent.XKMS, "XKMS Error without exception.");
	}
}
