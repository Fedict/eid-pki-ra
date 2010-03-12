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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.PersistenceException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.domain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.domain.Registration;
import be.fedict.eid.pkira.blm.model.domain.RegistrationStatus;
import be.fedict.eid.pkira.blm.model.domain.User;

/**
 * @author Bram Baeyens
 *
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
	private UserRepositoryBean userRepository;
	private CertificateDomainRepositoryBean certificateDomainRepository;
	private Registration valid;
	private User requester;
	private CertificateDomain certificateDomain;
	
	@BeforeClass
	public void setup() {
		registrationRepository = new RegistrationRepositoryBean();
		registrationRepository.setEntityManager(getEntityManager());
		userRepository = new UserRepositoryBean();
		userRepository.setEntityManager(getEntityManager());
		certificateDomainRepository = new CertificateDomainRepositoryBean();
		certificateDomainRepository.setEntityManager(getEntityManager());
		requester = createUser(NRN, FIRST_NAME, LAST_NAME);
		userRepository.persist(requester);
		certificateDomain = createCertificateDomain(DN_EXPRESSION, NAME);
		certificateDomainRepository.persist(certificateDomain);
	}

	@Test
	public void persist() throws Exception {		
		valid = createRegistration(EMAIL, certificateDomain, requester);
		registrationRepository.persist(valid);
		forceCommit();
		assertNotNull(valid.getId());
	}

	@Test(dependsOnMethods="persist", expectedExceptions=PersistenceException.class)
	public void userCertificateDomainConstraint() throws Exception {		
		Registration registration = createRegistration(EMAIL_2, certificateDomain, requester);
		registrationRepository.persist(registration);
		fail("PersistenceException expected.");
	}
	
	@Test(dependsOnMethods="persist")
	public void reject() throws Exception {
		registrationRepository.reject(valid);
		assertSame(RegistrationStatus.REJECTED, valid.getStatus());
	}
	
	@Test(dependsOnMethods="persist")
	public void confirm() throws Exception {
		registrationRepository.confirm(valid);
		assertSame(RegistrationStatus.CONFIRMED, valid.getStatus());
	}
	
	@Test(dependsOnMethods="persist")
	public void findAllNewRegistrations() throws Exception {
		List<Registration> newRegistrations = registrationRepository.findAllNewRegistrations();
		assertEquals(1, newRegistrations.size());
	}

	private Registration createRegistration(String email, CertificateDomain certificateDomain, User requester) {
		Registration registration = new Registration();
		registration.setStatus(RegistrationStatus.NEW);
		registration.setEmail(email);
		registration.setCertificateDomain(certificateDomain);
		registration.setRequester(requester);		
		return registration;
	}
	
	private User createUser(String nationalRegisterNumber, String firstName, String lastName) {
		User user = new User();
		user.setNationalRegisterNumber(nationalRegisterNumber);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		return user;
	}

	private CertificateDomain createCertificateDomain(String dnExpression, String name) {
		CertificateDomain certificateDomain = new CertificateDomain();
		certificateDomain.setDnExpression(dnExpression);
		certificateDomain.setName(name);
		return certificateDomain;
	}
}
