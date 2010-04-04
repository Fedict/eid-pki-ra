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
public class CertificateDomainValidatorTest {

	private static final String NAME = "name";
	private static final String DN_EXPRESSION = "dn";
	
	private static final CertificateDomain DOMAIN = createCertificateDomain();

	@Mock
	private CertificateDomainHome domainHome;

	@Mock
	private DistinguishedNameManager dnManager;

	private CertificateDomainValidator validator;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		validator = new CertificateDomainValidator();
		validator.setDistinguishedNameManager(dnManager);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateCertificateDomainHappyFlow() throws Exception {
		DOMAIN.setDnExpression(DN_EXPRESSION);
		when(validator.findByCertificateTypes(anySet())).thenReturn(Collections.singletonList(DOMAIN));
		DistinguishedName dn = mock(DistinguishedName.class);
		when(dnManager.createDistinguishedName(DN_EXPRESSION)).thenReturn(dn);
		when(dn.matches(any(DistinguishedName.class))).thenReturn(false);

		// Execute it
		validator.validate(DOMAIN);

		// Verify the results		
		verify(validator).findByName(NAME);
		verify(validator).findByCertificateTypes(DOMAIN.getCertificateTypes());
		verify(domainHome).persist();
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
		when(validator.findByName(NAME)).thenReturn(DOMAIN);

		// Execute it
		try {
			validator.validate(DOMAIN);			
			fail("Expected exception.");
		} catch (InvalidCertificateDomainNameException e) {
			// ok
		}
		verify(domainHome, never()).setInstance(any(CertificateDomain.class));
		verify(domainHome, never()).persist();
	}

	@Test
	public void testCreateCertificateDomainOverlap() throws Exception {
		DOMAIN.setDnExpression(DN_EXPRESSION);
		when(validator.findByCertificateTypes(DOMAIN.getCertificateTypes())).thenReturn(Collections.singletonList(DOMAIN));
		DistinguishedName dn = mock(DistinguishedName.class);
		when(dnManager.createDistinguishedName(DN_EXPRESSION)).thenReturn(dn);
		when(dn.matches(any(DistinguishedName.class))).thenReturn(true);

		// Execute it
		try {
			validator.validate(DOMAIN);			
			fail("Expected exception");
		} catch (DistinguishedNameOverlapsException e) {
			// ok
		}

		// Verify the results
		verify(domainHome, never()).setInstance(any(CertificateDomain.class));
		verify(domainHome, never()).persist();
	
	}
}
