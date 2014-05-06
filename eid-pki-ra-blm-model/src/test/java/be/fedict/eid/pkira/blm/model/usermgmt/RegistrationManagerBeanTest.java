/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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

import java.util.Collections;
import java.util.List;

import org.jboss.seam.log.Log;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.blacklist.BlacklistItem;
import be.fedict.eid.pkira.blm.model.blacklist.BlacklistRepository;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameExpression;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * @author Jan Van den Bergh
 */
public class RegistrationManagerBeanTest {

	private static final String TEST_RRN = "RRN";
	private static final String TEST_DN2 = "testDN2";
	private static final String TEST_DN1 = "testDN";
	private static final String RRN = "rrn";
	private static final String LAST_NAME = "last";
	private static final String FIRST_NAME = "first";
	private static final int DOMAIN_ID = 3;
	private static final String EMAIL_ADDRESS = "mail@mail.com";

	private static final CertificateDomain DOMAIN = new CertificateDomain();
	private static final User USER = new User();
	private static final String LOCALE = "en";

	private RegistrationManagerBean bean;
	@Mock
	private CertificateDomainHome certificateDomainHome;
	@Mock
	private RegistrationRepository registrationRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private Log log;
	@Mock
	private DistinguishedNameManager distinguishedNameManager;
	@Mock
	private UserHome userHome;
    @Mock
    private BlacklistRepository blacklistRepository;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new RegistrationManagerBean();
		bean.setCertificateDomainHome(certificateDomainHome);
		bean.setRegistrationRepository(registrationRepository);
		bean.setUserRepository(userRepository);
		bean.setLog(log);
		bean.setDistinguishedNameManager(distinguishedNameManager);
		bean.setUserHome(userHome);
        bean.setBlacklistRepository(blacklistRepository);
	}

	@Test
	public void testRegisterNewUser() throws RegistrationException {
		when(userRepository.findByNationalRegisterNumber(RRN)).thenReturn(null);
		when(certificateDomainHome.find()).thenReturn(DOMAIN);

		bean.registerUser(RRN, LAST_NAME, FIRST_NAME, DOMAIN_ID, EMAIL_ADDRESS, LOCALE);

		verify(userRepository).findByNationalRegisterNumber(RRN);
		verify(userRepository).persist(isA(User.class));
		verify(userRepository).getUserCount();
		verify(certificateDomainHome).setId(DOMAIN_ID);
		verify(certificateDomainHome).find();
		verify(registrationRepository).persist(isA(Registration.class));
		verifyNoMoreInteractions(userRepository, certificateDomainHome, userHome);
	}

	@Test
	public void testRegisterExistingUser() throws RegistrationException {
		when(userRepository.findByNationalRegisterNumber(RRN)).thenReturn(USER);
		when(certificateDomainHome.find()).thenReturn(DOMAIN);
		when(userHome.getInstance()).thenReturn(USER);

		bean.registerUser(RRN, LAST_NAME, FIRST_NAME, DOMAIN_ID, EMAIL_ADDRESS, LOCALE);

		verify(userRepository).findByNationalRegisterNumber(RRN);
		verify(userRepository).getUserCount();
		verify(certificateDomainHome).setId(DOMAIN_ID);
		verify(certificateDomainHome).find();
		verify(userHome).setInstance(USER);
		verify(userHome).getInstance();
		verify(userHome).update();
		verifyNoMoreInteractions(userRepository, certificateDomainHome, userHome);
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
		DistinguishedNameExpression dn2 = mock(DistinguishedNameExpression.class);
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(Collections.singletonList(registration));
		when(distinguishedNameManager.createDistinguishedName(TEST_DN1)).thenReturn(dn1);
		when(distinguishedNameManager.createDistinguishedNameExpression(TEST_DN2)).thenReturn(dn2);
		when(dn2.matches(eq(dn1))).thenReturn(true);
		
		List<Registration> theRegistrations = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, Collections.<String>emptyList(), CertificateType.CLIENT);
		assertEquals(theRegistrations, Collections.singletonList(registration));
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
		DistinguishedNameExpression dn2 = mock(DistinguishedNameExpression.class);
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(Collections.singletonList(registration));
		when(distinguishedNameManager.createDistinguishedName(TEST_DN1)).thenReturn(dn1);
		when(distinguishedNameManager.createDistinguishedNameExpression(TEST_DN2)).thenReturn(dn2);
		when(dn2.matches(eq(dn1))).thenReturn(true);
		
		List<Registration> theRegistrations = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, Collections.<String>emptyList(), CertificateType.SERVER);
		assertEquals(theRegistrations.size(), 0);
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
		DistinguishedNameExpression dn2 = mock(DistinguishedNameExpression.class);
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(Collections.singletonList(registration));
		when(distinguishedNameManager.createDistinguishedName(TEST_DN1)).thenReturn(dn1);
		when(distinguishedNameManager.createDistinguishedNameExpression(TEST_DN2)).thenReturn(dn2);
		when(dn2.matches(eq(dn1))).thenReturn(false);
		
		List<Registration> theRegistrations = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, Collections.<String>emptyList(), CertificateType.CLIENT);
		assertEquals(theRegistrations.size(), 0);
	}
	
	@Test
	public void testCheckAuthorizationForUserAndDNNoDomains() throws Exception {
		User user = new User();						
		
		when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
		List<Registration> emptyList = Collections.emptyList();
		when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(emptyList);		
		
		List<Registration> theRegistrations = bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, Collections.<String>emptyList(), CertificateType.CLIENT);
		assertEquals(theRegistrations.size(), 0);
	}

    @Test
    public void blacklistedCNsAreNotAllowed() throws InvalidDistinguishedNameException {
        User user = new User();
        CertificateDomain domain = new CertificateDomain();
        domain.setDnExpression(TEST_DN2);
        domain.setClientCertificate(true);
        Registration registration = new Registration();
        registration.setCertificateDomain(domain);
        registration.setRequester(user);

        BlacklistItem blackListItem = new BlacklistItem();
        blackListItem.setBlockedCN(".google.com");

        DistinguishedName dn1 = mock(DistinguishedName.class);
        DistinguishedNameExpression dn2 = mock(DistinguishedNameExpression.class);

        when(userRepository.findByNationalRegisterNumber(eq(TEST_RRN))).thenReturn(user);
        when(registrationRepository.findApprovedRegistrationsByUser(eq(user))).thenReturn(Collections.singletonList(registration));
        when(distinguishedNameManager.createDistinguishedName(TEST_DN1)).thenReturn(dn1);
        when(distinguishedNameManager.createDistinguishedNameExpression(TEST_DN2)).thenReturn(dn2);
        when(dn2.matches(eq(dn1))).thenReturn(true);
        when(dn1.getPart("cn")).thenReturn(Collections.singleton("www.google.com"));

        when(blacklistRepository.getAllBlacklistItemsForRegistration(registration)).thenReturn(Collections.singletonList(blackListItem));

        try {
            bean.findRegistrationForUserDNAndCertificateType(TEST_RRN, TEST_DN1, Collections.<String>emptyList(), CertificateType.CLIENT);
            fail("Expected blacklisted cn");
        } catch (BlacklistedException e) {
            // Ok!
        }
    }
}
