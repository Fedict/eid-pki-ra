/**
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

package be.fedict.eid.pkira.blm.model.certificatedomain;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;

/**
 * @author Bram Baeyens
 */
public class CertificateDomainRepositoryBeanTest extends DatabaseTest {

	private CertificateDomainRepositoryBean repository;
	private CertificateDomain valid;

	private static final String DN_EXPRESSION = "cdTestDnExpression";
	private static final String DN_EXPRESSION_2 = "cdTestDnExpression2";
	private static final String DN_EXPRESSION_UNKNOWN = "cdTestDnExpressionUnknown";
	private static final String NAME = "cdTestName";
	private static final String NAME_UNKNOWN = "cdTestNameUnknown";

	@BeforeClass
	public void setup() {
		repository = new CertificateDomainRepositoryBean();
		repository.setEntityManager(getEntityManager());
	}

	@Test
	public void persist() throws Exception {
		valid = createCertificateDomain(DN_EXPRESSION, NAME);
		repository.persist(valid);
		forceCommit();
		assertNotNull(valid.getId());
	}

	@Test(dependsOnMethods = "persist", expectedExceptions = PersistenceException.class)
	public void nameConstraint() throws Exception {
		repository.persist(createCertificateDomain(DN_EXPRESSION_2, NAME));
		fail("PersistenceException expected");
	}

	@Test(dependsOnMethods = "persist")
	public void findById() throws Exception {
		CertificateDomain certificateDomain = repository.findById(valid.getId());
		assertEquals(certificateDomain, valid);
	}

	@Test(dependsOnMethods = "persist")
	public void findByNonExistingId() throws Exception {
		CertificateDomain certificateDomain = repository.findById(Integer.valueOf(987654));
		assertNull(certificateDomain);
	}

	@Test(dependsOnMethods = "persist")
	public void findByName() throws Exception {
		CertificateDomain certificateDomain = repository.findByName(NAME);
		assertEquals(certificateDomain, valid);
	}

	@Test(dependsOnMethods = "persist")
	public void findByNonExistingName() throws Exception {
		assertNull(repository.findByName(NAME_UNKNOWN));
	}

	@Test(dependsOnMethods = "persist")
	public void findByDnExpression() throws Exception {
		CertificateDomain certificateDomain = repository.findByDnExpression(DN_EXPRESSION);
		assertEquals(certificateDomain, valid);
	}

	@Test(dependsOnMethods = "persist")
	public void findByNonExistingDnExpression() throws Exception {
		assertNull(repository.findByDnExpression(DN_EXPRESSION_UNKNOWN));
	}

	@Test(dependsOnMethods = "persist")
	public void findByCertificateTypes() throws Exception {
		Set<CertificateType> certificateTypes = new HashSet<CertificateType>();
		for (CertificateType type : CertificateType.values()) {
			certificateTypes.add(type);
		}
		List<CertificateDomain> domains = repository.findByCertificateTypes(certificateTypes);
		assertNotNull(domains);
		assertEquals(domains.size(), 1);
		assertTrue(domains.contains(valid));
	}

	private CertificateDomain createCertificateDomain(String dnExpression, String name) {
		CertificateDomain certificateDomain = new CertificateDomain();
		certificateDomain.setDnExpression(dnExpression);
		certificateDomain.setName(name);
		certificateDomain.setClientCertificate(true);
		return certificateDomain;
	}
}
