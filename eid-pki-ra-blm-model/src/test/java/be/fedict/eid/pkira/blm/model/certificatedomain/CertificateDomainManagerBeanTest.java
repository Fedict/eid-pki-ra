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
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Collections;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;

/**
 * @author Jan Van den Bergh
 */
public class CertificateDomainManagerBeanTest {

	private static final String NAME = "name";
	private static final String DN_EXPRESSION = "dn";
	
	private static final CertificateDomain DOMAIN = createCertificateDomain();

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

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateCertificateDomainHappyFlow() throws Exception {
		DOMAIN.setDnExpression(DN_EXPRESSION);
		when(domainRepository.findByCertificateTypes(anySet())).thenReturn(Collections.singletonList(DOMAIN));
		DistinguishedName dn = mock(DistinguishedName.class);
		when(dnManager.createDistinguishedName(DN_EXPRESSION)).thenReturn(dn);
		when(dn.matches(any(DistinguishedName.class))).thenReturn(false);

		// Execute it
		bean.saveCertificateDomain(DOMAIN);

		// Verify the results		
		verify(domainRepository).findByName(NAME);
		verify(domainRepository).findByCertificateTypes(DOMAIN.getCertificateTypes());
		verify(domainRepository).persist(DOMAIN);
	}

	private static CertificateDomain createCertificateDomain() {
		CertificateDomain result = new CertificateDomain();
		result.setName(NAME);
		result.setDnExpression(DN_EXPRESSION);
		result.setClientCertificate(true);
		return result;
	}

	@Test
	public void testCreateCertificateDomainDuplicateName() throws Exception {
		// Prepare the test
		when(domainRepository.findByName(NAME)).thenReturn(DOMAIN);

		// Execute it
		try {
			bean.saveCertificateDomain(DOMAIN);
			fail("Expected exception.");
		} catch (InvalidCertificateDomainNameException e) {
			// ok
		}

		verify(domainRepository, never()).persist(any(CertificateDomain.class));
	}

	@Test
	public void testCreateCertificateDomainOverlap() throws Exception {
		DOMAIN.setDnExpression(DN_EXPRESSION);
		when(domainRepository.findByCertificateTypes(DOMAIN.getCertificateTypes())).thenReturn(Collections.singletonList(DOMAIN));
		DistinguishedName dn = mock(DistinguishedName.class);
		when(dnManager.createDistinguishedName(DN_EXPRESSION)).thenReturn(dn);
		when(dn.matches(any(DistinguishedName.class))).thenReturn(true);

		// Execute it
		try {
			bean.saveCertificateDomain(DOMAIN);
			fail("Expected exception");
		} catch (DistinguishedNameOverlapsException e) {
			// ok
		}

		// Verify the results
		verify(domainRepository, never()).persist(any(CertificateDomain.class));
	
	}
}
