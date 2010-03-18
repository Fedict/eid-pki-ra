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
package be.fedict.eid.pkira.blm.model.certificatedomain;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Collections;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.domain.CertificateType;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;

/**
 * @author Jan Van den Bergh
 */
public class CertificateDomainManagerBeanTest {

	/**
	 * 
	 */
	private static final Set<CertificateType> TYPES = Collections.singleton(CertificateType.CLIENT);
	/**
	 * 
	 */
	private static final CertificateDomain DOMAIN = new CertificateDomain();
	private static final String NAME = "name";
	private static final String CA_ID = "ca-1";
	private static final String DN_EXPRESSION = "dn";

	@Mock
	private CertificateDomainRepository domainRepository;

	@Mock
	private DistinguishedNameManager dnManager;

	private CertificateDomainManagerBean bean;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new CertificateDomainManagerBean();
		bean.setDistinguishedNameManager(dnManager);
		bean.setDomainRepository(domainRepository);
	}

	@Test
	public void testCreateCertificateDomainHappyFlow() throws Exception {
		DOMAIN.setDnExpression(DN_EXPRESSION);
		when(domainRepository.findByCertificateTypes(TYPES)).thenReturn(Collections.singletonList(DOMAIN));
		DistinguishedName dn = mock(DistinguishedName.class);
		when(dnManager.createDistinguishedName(DN_EXPRESSION)).thenReturn(dn);
		when(dn.matches(any(DistinguishedName.class))).thenReturn(false);

		// Execute it
		CertificateDomain domain2 = bean.registerCertificateDomain(NAME, CA_ID, DN_EXPRESSION, TYPES);

		// Verify the results
		assertNotNull(domain2);
		assertEquals(domain2.getDnExpression(), DN_EXPRESSION);
		assertEquals(domain2.getName(), NAME);
		assertTrue(domain2.isForClientCertificate());
		assertFalse(domain2.isForServerCertificate());
		assertFalse(domain2.isForCodeSigningCertificate());
		verify(domainRepository).findByName(NAME);
		verify(domainRepository).findByCertificateTypes(TYPES);
		verify(domainRepository).persist(domain2);
	}

	@Test
	public void testCreateCertificateDomainDuplicateName() throws Exception {
		// Prepare the test
		when(domainRepository.findByName(NAME)).thenReturn(DOMAIN);

		// Execute it
		try {
			bean.registerCertificateDomain(NAME, CA_ID, DN_EXPRESSION, TYPES);
			fail("Expected exception.");
		} catch (DuplicateCertificateDomainNameException e) {
			// ok
		}

		verify(domainRepository, never()).persist(any(CertificateDomain.class));
	}

	@Test
	public void testCreateCertificateDomainOverlap() throws Exception {
		DOMAIN.setDnExpression(DN_EXPRESSION);
		when(domainRepository.findByCertificateTypes(TYPES)).thenReturn(Collections.singletonList(DOMAIN));
		DistinguishedName dn = mock(DistinguishedName.class);
		when(dnManager.createDistinguishedName(DN_EXPRESSION)).thenReturn(dn);
		when(dn.matches(any(DistinguishedName.class))).thenReturn(true);

		// Execute it
		try {
			bean.registerCertificateDomain(NAME, CA_ID, DN_EXPRESSION, TYPES);
			fail("Expected exception");
		} catch (DistinguishedNameOverlapsException e) {
			// ok
		}

		// Verify the results
		verify(domainRepository, never()).persist(any(CertificateDomain.class));
	
	}
}
