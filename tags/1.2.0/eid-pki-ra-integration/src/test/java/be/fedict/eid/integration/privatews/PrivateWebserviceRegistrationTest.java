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
package be.fedict.eid.integration.privatews;

import static be.fedict.eid.integration.util.WebServiceFactory.getPrivateWebServiceClient;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.privatews.UserWS;

/**
 * Test of the public web service.
 * 
 * @author Jan Van den Bergh
 */
public class PrivateWebserviceRegistrationTest {	
	
	@Test
	public void findUserTest() {
		UserWS user = getPrivateWebServiceClient().findUser("");
		assertNull(user);
	}

}
