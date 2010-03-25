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

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainRepository;

/**
 * @author Jan Van den Bergh
 */
public class RegistrationManagerBeanTest {

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

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new RegistrationManagerBean();
		bean.setCertificateDomainRepository(certificateDomainRepository);
		bean.setRegistrationRepository(registrationRepository);
		bean.setUserRepository(userRepository);
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
}
