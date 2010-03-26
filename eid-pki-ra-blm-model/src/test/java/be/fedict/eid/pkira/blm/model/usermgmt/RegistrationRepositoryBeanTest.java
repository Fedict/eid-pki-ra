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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.testng.Assert.assertTrue;

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

	private static final String NRN = "rTestNRN";
	private static final String FIRST_NAME = "rTestFN";
	private static final String LAST_NAME = "rTestLN";
	private static final String DN_EXPRESSION = "rTestDnExpression";
	private static final String NAME = "rTestName";
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
		
		requester = createPersistedUser(NRN, FIRST_NAME, LAST_NAME);
		certificateDomain = createPersistedCertificateDomain(DN_EXPRESSION, NAME);
	}

	@Test
	public void persist() throws Exception {
		valid = createRegistration(EMAIL, certificateDomain, requester);
		registrationRepository.persist(valid);
		forceCommit();
		assertNotNull(valid.getId());
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
		assertSame(RegistrationStatus.DISAPPROVED, valid.getStatus());
	}

	@Test(dependsOnMethods = "persist")
	public void confirm() throws Exception {
		registrationRepository.setApproved(valid);
		assertSame(RegistrationStatus.APPROVED, valid.getStatus());
	}

	@Test(dependsOnMethods = "persist")
	public void findAllNewRegistrations() throws Exception {
		List<Registration> newRegistrations = registrationRepository.findAllNewRegistrations();
		assertTrue(newRegistrations.size()>=1);
	}
	
	@Test(dependsOnMethods="persist") 
	public void findRegistration() throws Exception {
		Registration registration = registrationRepository.findRegistration(certificateDomain, requester);
		assertNotNull(registration);
	}

	private Registration createRegistration(String email, CertificateDomain certificateDomain, User requester) {
		Registration registration = new Registration();
		registration.setStatus(RegistrationStatus.NEW);
		registration.setEmail(email);
		registration.setCertificateDomain(certificateDomain);
		registration.setRequester(requester);
		return registration;
	}

	private User createPersistedUser(String nationalRegisterNumber, String firstName, String lastName) {
		User user = new User();
		user.setNationalRegisterNumber(nationalRegisterNumber);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		persistObject(user);
		return user;
	}

	private CertificateDomain createPersistedCertificateDomain(String dnExpression, String name) {
		final CertificateDomain certificateDomain = new CertificateDomain();
		certificateDomain.setDnExpression(dnExpression);
		certificateDomain.setName(name);

		persistObject(certificateDomain);
		return certificateDomain;
	}
}
