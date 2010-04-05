/*
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

package be.fedict.eid.pkira.xkmsws;

import static org.junit.Assert.assertNotNull;

import org.testng.annotations.Test;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSService;

public class XKMSServiceFactoryTest {

	@Test
	public void testGetInstance() {
		// operate
		XKMSService service = XKMSServiceFactory.getInstance();

		// verify
		assertNotNull(service);
	}
}
