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
package be.fedict.eid.integration.ws;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.privatews.CreateCertificateDomainResponse;
import be.fedict.eid.pkira.generated.privatews.CreateCertificateDomainResult;

/**
 * @author Jan Van den Bergh
 */
public class PrivateWebserviceCertificateDomainTest {

	private static final String EXPRESSION = "c=be,ou=test,cn=*";
	private static final String EXPRESSION_OVERLAPS = "c=be,ou=*,cn=*";
	private static final String EXPRESSION_INVALID = "blablabla";
	private static final String NAME = "testCertificateDomain";
	private static final String NAME2 = "testCertificateDomain2";

	@Test
	public void testCreateCertificateDomain() {
		CreateCertificateDomainResponse response = WebServiceFactory.getPrivateWebServiceClient()
				.createCertificateDomain(NAME, EXPRESSION, true, true, true);

		assertNotNull(response);
		assertEquals(CreateCertificateDomainResult.SUCCESS, response.getResult());
		assertNotNull(response.getDomainId());
	}

	@Test(dependsOnMethods = "testCreateCertificateDomain")
	public void testCreateCertificateDomainAgain() {
		CreateCertificateDomainResponse response = WebServiceFactory.getPrivateWebServiceClient()
				.createCertificateDomain(NAME, EXPRESSION, true, true, true);

		assertNotNull(response);
		assertEquals(CreateCertificateDomainResult.DUPLICATE_NAME, response.getResult());
	}

	@Test(dependsOnMethods = "testCreateCertificateDomain")
	public void testCreateCertificateOverlaps() {
		CreateCertificateDomainResponse response = WebServiceFactory.getPrivateWebServiceClient()
				.createCertificateDomain(NAME2, EXPRESSION_OVERLAPS, true, true, true);

		assertNotNull(response);
		assertEquals(CreateCertificateDomainResult.INVALID_DN, response.getResult());
	}

	@Test
	public void testCreateCertificateInvalidDN() {
		CreateCertificateDomainResponse response = WebServiceFactory.getPrivateWebServiceClient()
				.createCertificateDomain(NAME2, EXPRESSION_INVALID, true, true, true);

		assertNotNull(response);
		assertEquals(CreateCertificateDomainResult.INVALID_DN, response.getResult());
	}
}
