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

package be.fedict.eid.pkira.blm.model.usermgmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;

/**
 * @author Bram Baeyens
 */
public class RegistrationRepositoryBeanTest extends DatabaseTest {

	private static final String EMAIL = "rAbc@de.fg";
	private static final String EMAIL_2 = "rAbc@de.fg2";
	
	private RegistrationRepositoryBean registrationRepository;
	private Registration valid;
	private User requester;
	public CertificateDomain certificateDomain;

	@BeforeClass(dependsOnGroups = "CertificateDomainRepository")
	public void setup() {
		registrationRepository = new RegistrationRepositoryBean();
		registrationRepository.setEntityManager(getEntityManager());

		requester = loadObject(User.class, TEST_USER_ID);
		certificateDomain = loadObject(CertificateDomain.class, TEST_CERTIFICATE_DOMAIN_ID);
	}	

	@Test
	public void persist() throws Exception {
		valid = createRegistration(EMAIL, certificateDomain, requester);
		registrationRepository.persist(valid);
		forceCommit();
		assertNotNull(valid.getId());		
	}
	
	@Test(dependsOnMethods="persist")
	public void testGetRegistrations() {
		User requester2 = getEntityManager().getReference(User.class, requester.getId());
		Collection<Registration> registrations = requester2.getRegistrations();
		assertNotNull(registrations);
		assertEquals(3, registrations.size());
	}

	@Test(dependsOnMethods = "persist", expectedExceptions = PersistenceException.class)
	public void userCertificateDomainConstraint() throws Exception {
		Registration registration = createRegistration(EMAIL_2, certificateDomain, requester);
		registrationRepository.persist(registration);
		fail("PersistenceException expected.");
	}

	@Test(dependsOnMethods = "persist")
	public void reject() throws Exception {
		registrationRepository.setDisapproved(valid);
		assertFalse(getEntityManager().contains(valid));
	}

	@Test(dependsOnMethods = "persist")
	public void confirm() throws Exception {
		registrationRepository.setApproved(valid);
		assertSame(RegistrationStatus.APPROVED, valid.getStatus());
	}

	@Test(dependsOnMethods = "persist")
	public void findAllNewRegistrations() throws Exception {
		List<Registration> newRegistrations = registrationRepository.findAllNewRegistrations();
		assertTrue(newRegistrations.size() >= 1);
	}

	@Test(dependsOnMethods = "persist")
	public void findRegistration() throws Exception {
		Registration registration = registrationRepository.findRegistration(certificateDomain, requester);
		assertNotNull(registration);
	}
	
	@Test(dependsOnMethods="persist")
	public void getNumberOfRegistrationsForForUserInStatus() {
		assertTrue(registrationRepository.getNumberOfRegistrationsForForUserInStatus(requester, RegistrationStatus.NEW)>0);
	}
	
	@Test
	public void findApprovedRegistrationsByUser() {
		List<Registration> registrations  = registrationRepository.findApprovedRegistrationsByUser(requester);
		
		assertNotNull(registrations);
		assertTrue(registrations.size()==1);
		
	}

	private Registration createRegistration(String email, CertificateDomain certificateDomain, User requester) {
		Registration registration = new Registration();
		registration.setStatus(RegistrationStatus.NEW);
		registration.setEmail(email);
		registration.setCertificateDomain(certificateDomain);
		registration.setRequester(requester);
		return registration;
	}
}
