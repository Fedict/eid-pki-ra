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
package be.fedict.eid.pkira.blm.model.usermgmt;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Collections;
import java.util.List;

import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainRepository;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;

/**
 * @author Jan Van den Bergh
 */
public class RegistrationManagerBeanTest {

	/**
	 * 
	 */
	private static final String TEST_RRN = "RRN";
	/**
	 * 
	 */
	private static final String TEST_DN2 = "testDN2";
	/**
	 * 
	 */
	private static final String TEST_DN1 = "testDN";
	private static final String RRN = "rrn";
	private static final String LAST_NAME = "last";
	private static final String FIRST_NAME = "first";
	private static final int DOMAIN_ID = 3;
	private static final String EMAIL_ADDRESS = "mail@mail.com";

	private static final CertificateDomain DOMAIN = new CertificateDomain();
	private static final User USER = new User();

	private RegistrationManagerBean bean;
	@Mock
	private CertificateDomainRepository certificateDomainRepository;
	@Mock
	private RegistrationRepository registrationRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private Log log;
	@Mock
	private DistinguishedNameManager distinguishedNameManager;
	@Mock
	private MailTemplate mailTemplate;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new RegistrationManagerBean();
		bean.setCertificateDomainRepository(certificateDomainRepository);
		bean.setRegistrationRepository(registrationRepository);
		bean.setUserRepository(userRepository);
		bean.setLog(log);
		bean.setDistinguishedNameManager(distinguishedNameManager);
		bean.setMailTemplate(mailTemplate);
	}

	@Test
	public void testRegisterNewUser() throws RegistrationException {
		when(userRepository.findByNationalRegisterNumber(RRN)).thenReturn(null);
		when(certificateDomainRepository.findById(DOMAIN_ID)).thenReturn(DOMAIN);

		bean.registerUser(RRN, LAST_NAME, FIRST_NAME, DOMAIN_ID, EMAIL_ADDRESS);

		verify(userRepository).findByNationalRegisterNumber(RRN);
		verify(userRepository).persist(isA(User.class));
		verify(certificateDomainRepository).findById(DOMAIN_ID);
		verify(registrationRepository).persist(isA(Registration.class));
		verifyNoMoreInteractions(userRepository, certificateDomainRepository);
	}

	@Test
	public void testRegisterExistingUser() throws RegistrationException {
		when(userRepository.findByNationalRegisterNumber(RRN)).thenReturn(USER);
		when(certificateDomainRepository.findById(DOMAIN_ID)).thenReturn(DOMAIN);

		bean.registerUser(RRN, LAST_NAME, FIRST_NAME, DOMAIN_ID, EMAIL_ADDRESS);

		verify(userRepository).findByNationalRegisterNumber(RRN);
		verify(certificateDomainRepository).findById(DOMAIN_ID);
		verify(registrationRepository).persist(isA(Registration.class));
		verifyNoMoreInteractions(userRepository, certificateDomainRepository);
	}

	@Test
	public void testCheckAuthorizationForUserAndDN() throws Exception {
		User user = new User();				
		CertificateDomain domain = new CertificateDomain();
		domain.setDnExpression(TEST_DN2);
		domain.setClientCertificate(true);
		Registration registration = new Registration();
		registration.setCertificateDomain(domain);
		registration.setRequester(user);
		
		DistinguishedName dn1 = mock(DistinguishedName.class);
		DistinguishedName dn2 = mock(DistinguishedName.class);
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(Collections.singletonList(registration));
		when(distinguishedNameManager.createDistinguishedName(TEST_DN1)).thenReturn(dn1);
		when(distinguishedNameManager.createDistinguishedName(TEST_DN2)).thenReturn(dn2);
		when(dn2.matches(eq(dn1))).thenReturn(true);
		
		Registration theRegistration = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, CertificateType.CLIENT);
		assertEquals(theRegistration, registration);
	}
	
	@Test
	public void testCheckAuthorizationForUserAndDNInvalidCertificateType() throws Exception {
		User user = new User();				
		CertificateDomain domain = new CertificateDomain();
		domain.setDnExpression(TEST_DN2);
		domain.setClientCertificate(true);
		Registration registration = new Registration();
		registration.setCertificateDomain(domain);
		registration.setRequester(user);
		
		DistinguishedName dn1 = mock(DistinguishedName.class);
		DistinguishedName dn2 = mock(DistinguishedName.class);
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(Collections.singletonList(registration));
		when(distinguishedNameManager.createDistinguishedName(TEST_DN1)).thenReturn(dn1);
		when(distinguishedNameManager.createDistinguishedName(TEST_DN2)).thenReturn(dn2);
		when(dn2.matches(eq(dn1))).thenReturn(true);
		
		Registration theRegistration = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, CertificateType.SERVER);
		assertNull(theRegistration);
	}
	
	@Test
	public void testCheckAuthorizationForUserAndDNNoMatch() throws Exception {
		User user = new User();						
		CertificateDomain domain = new CertificateDomain();
		domain.setDnExpression(TEST_DN2);
		domain.setClientCertificate(true);
		Registration registration = new Registration();
		registration.setCertificateDomain(domain);
		registration.setRequester(user);
		
		DistinguishedName dn1 = mock(DistinguishedName.class);
		DistinguishedName dn2 = mock(DistinguishedName.class);
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(Collections.singletonList(registration));
		when(distinguishedNameManager.createDistinguishedName(TEST_DN1)).thenReturn(dn1);
		when(distinguishedNameManager.createDistinguishedName(TEST_DN2)).thenReturn(dn2);
		when(dn2.matches(eq(dn1))).thenReturn(false);
		
		Registration theRegistration = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, CertificateType.CLIENT);
		assertNull(theRegistration);
	}
	
	@Test
	public void testCheckAuthorizationForUserAndDNNoDomains() throws Exception {
		User user = new User();						
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		List<Registration> emptyList = Collections.emptyList();
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(emptyList);		
		
		Registration theRegistration = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, CertificateType.CLIENT);
		assertNull(theRegistration);
	}
}
