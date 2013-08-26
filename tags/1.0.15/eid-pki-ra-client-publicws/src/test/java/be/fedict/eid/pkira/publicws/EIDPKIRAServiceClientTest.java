/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
package be.fedict.eid.pkira.publicws;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.publicws.EIDPKIRAPortType;

public class EIDPKIRAServiceClientTest {

	private static final String SIGNED_CONTRACT = "SIGNED CONTRACT";
	private static final String CONTRACT = "CONTRACT";

	private EIDPKIRAPortType port;
	private EIDPKIRAServiceClient serviceClient;

	@BeforeMethod
	public void setup() {
		port = mock(EIDPKIRAPortType.class);

		serviceClient = new EIDPKIRAServiceClient();
		serviceClient.setWebservicePort(port);
	}

	@Test
	public void testSignCertificate() {
		// Prepare
		when(port.signCertificate(eq(CONTRACT))).thenReturn(SIGNED_CONTRACT);
		
		// Call
		String result = serviceClient.signCertificate(CONTRACT);
		
		// Verify
		verify(port).signCertificate(eq(CONTRACT));
		verifyNoMoreInteractions(port);
		
		assertEquals(result, SIGNED_CONTRACT);
	}
	
	@Test
	public void testRevokeCertificate() {
		// Prepare
		when(port.revokeCertificate(eq(CONTRACT))).thenReturn(SIGNED_CONTRACT);
		
		// Call
		String result = serviceClient.revokeCertificate(CONTRACT);
		
		// Verify
		verify(port).revokeCertificate(eq(CONTRACT));
		verifyNoMoreInteractions(port);
		
		assertEquals(result, SIGNED_CONTRACT);
	}
}
