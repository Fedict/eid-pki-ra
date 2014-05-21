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

package be.fedict.eid.pkira.blm.model.reporting.jmx;

import java.util.Arrays;
import java.util.List;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertEquals;

public class PKIRAStatCountersTest extends DatabaseTest {

	private PKIRAStatCounters counters;

	@BeforeMethod
	public void beforeMethod() {
		MockitoAnnotations.initMocks(this);

		counters = spy(new PKIRAStatCounters());
		doReturn(getEntityManager()).when(counters).getEntityManager();
	}

	@Test
	public void getCertificateDomains() {
		List<String> certificateDomains = counters.getCertificateDomains();
		assertEquals(certificateDomains, Arrays.asList("eHealth Client Certificates", "eHealth Server Certificates",
				"eHealth Code Signing Certificates", "Test", "eHealth Persons Certificates"));
	}

	@Test
	public void getFailedRequestCount() {
		assertEquals(counters.getFailedRequestCount("Test"), 1L);
	}

	@Test
	public void getSuccesfulRequestCount() {
		assertEquals(counters.getSuccessfulRequestCount("Test"), 2L);
	}
}
