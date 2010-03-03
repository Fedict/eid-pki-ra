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
package be.fedict.eid.pkira.blm.model.domain;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import javax.persistence.EntityManager;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Jan Van den Bergh
 *
 */
public class DomainRepositoryBeanTest {

	private DomainRepositoryBean bean;
	private EntityManager entityManager;
	
	@BeforeMethod
	public void setup() {
		entityManager = mock(EntityManager.class);
		
		bean = new DomainRepositoryBean();
		bean.setEntityManager(entityManager);
	}
	
	@Test
	public void testPersistCertificate() {
		Certificate certificate = new Certificate();
		bean.persistCertificate(certificate);	
		
		verify(entityManager).persist(eq(certificate));
		verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void testPersistContract() {
		CertificateSigningContract contract = new CertificateSigningContract();
		bean.persistContract(contract);	
		
		verify(entityManager).persist(eq(contract));
		verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void testFindAllCertificates() throws Exception {
		// TODO find a way to test this.
	}

}
