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
package be.fedict.eid.pkira.blm.model.mappers;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.generated.privatews.CertificateDomainWS;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;

/**
 * @author Jan Van den Bergh
 */
public class CertificateDomainMapperTest {

	private static final String TEST_ISSUER = "Issuser";
	private final CertificateDomainMapperBean bean = new CertificateDomainMapperBean();

	/**
	 * Test method for
	 * {@link be.fedict.eid.pkira.blm.model.mappers.CertificateMapperBean#map(be.fedict.eid.pkira.blm.model.contracts.CertificateType)}
	 * .
	 */
	@Test
	public void testMapCertificateDomain() {
		CertificateDomain domain = createTestCertificateDomain();
		
		CertificateDomainWS domainWS = bean.map(domain);
		assertNotNull(domainWS);
		assertNull(domainWS.getId());
		assertEquals(domainWS.getName(), domain.getName());
		assertEquals(1, domainWS.getCertificateTypes().size());
		assertEquals(CertificateTypeWS.CLIENT, domainWS.getCertificateTypes().get(0));		

		assertNull(bean.map((CertificateDomain) null));
	}

	/**
	 * Test method for
	 * {@link be.fedict.eid.pkira.blm.model.mappers.CertificateMapperBean#map(be.fedict.eid.pkira.blm.model.contracts.CertificateType)}
	 * .
	 */
	@Test
	public void testMapCertificateDomains() {
		CertificateDomain domain = createTestCertificateDomain();
		
		List<CertificateDomainWS> domainsWS = bean.map(Collections.singleton(domain));
		assertNotNull(domainsWS);
		assertEquals(1, domainsWS.size());
		assertNotNull(domainsWS.get(0));		

		List<CertificateDomain> nullList = null;
		List<CertificateDomainWS> nullListWS = bean.map(nullList);
		assertNotNull(nullListWS);
		assertEquals(0, nullListWS.size());
	}

	private CertificateDomain createTestCertificateDomain() {
		CertificateDomain domain = new CertificateDomain();
		domain.setName(TEST_ISSUER);
		domain.setClientCertificate(true);		
		
		return domain;
	}

}
