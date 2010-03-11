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

package be.fedict.eid.pkira.blm.model.jpa;

import static org.testng.Assert.*;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.domain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.domain.DatabaseTest;

/**
 * @author Bram Baeyens
 *
 */
public class CertificateDomainRepositoryBeanTest extends DatabaseTest {
	
	private CertificateDomainRepositoryBean repository;
	private CertificateDomain valid;
	
	private static final String DN_EXPRESSION = "cdTestDnExpression";
	private static final String DN_EXPRESSION_2 = "cdTestDnExpression2";
	private static final String DN_EXPRESSION_UNKNOWN = "cdTestDnExpressionUnknown";
	private static final String NAME = "cdTestName";
	private static final String NAME_2 = "cdTestName2";
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
	
	@Test(dependsOnMethods="persist", expectedExceptions=PersistenceException.class)
	public void dnExpressionConstraint() throws Exception {
		repository.persist(createCertificateDomain(DN_EXPRESSION, NAME_2));
		fail("PersistenceException expected");
	}
	
	@Test(dependsOnMethods="persist", expectedExceptions=PersistenceException.class)
	public void nameConstraint() throws Exception {
		repository.persist(createCertificateDomain(DN_EXPRESSION_2, NAME));
		fail("PersistenceException expected");
	}
	
	@Test(dependsOnMethods="persist")
	public void findById() throws Exception {
		CertificateDomain certificateDomain = repository.findById(valid.getId());
		assertEquals(certificateDomain, valid);
	}
	
	@Test(dependsOnMethods="persist")
	public void findByNonExistingId() throws Exception {
		CertificateDomain certificateDomain = repository.findById(Integer.valueOf(987654));
		assertNull(certificateDomain);
	}
	
	@Test(dependsOnMethods="persist")
	public void findByName() throws Exception {
		CertificateDomain certificateDomain = repository.findByName(NAME);
		assertEquals(certificateDomain, valid);
	}
	
	@Test(dependsOnMethods="persist", expectedExceptions=NoResultException.class)
	public void findByNonExistingName() throws Exception {
		repository.findByName(NAME_UNKNOWN);
		fail("NoResultException expected");
	}
	
	@Test(dependsOnMethods="persist")
	public void findByDnExpression() throws Exception {
		CertificateDomain certificateDomain = repository.findByDnExpression(DN_EXPRESSION);
		assertEquals(certificateDomain, valid);
	}
	
	@Test(dependsOnMethods="persist", expectedExceptions=NoResultException.class)
	public void findByNonExistingDnExpression() throws Exception {
		repository.findByDnExpression(DN_EXPRESSION_UNKNOWN);
		fail("NoResultException expected");
	}

	private CertificateDomain createCertificateDomain(String dnExpression, String name) {
		CertificateDomain certificateDomain = new CertificateDomain();
		certificateDomain.setDnExpression(dnExpression);
		certificateDomain.setName(name);
		return certificateDomain;
	}
}
