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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.EmailValidator;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameExpression;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;

/**
 * Registration manager implementation.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(RegistrationManager.NAME)
public class RegistrationManagerBean implements RegistrationManager {

	@Logger
	private Log log;

	@In(value = RegistrationRepository.NAME, create = true)
	private RegistrationRepository registrationRepository;

	@In(value = RegistrationHome.NAME, create = true)
	private RegistrationHome registrationHome;

	@In(value = CertificateDomainHome.NAME, create = true)
	private CertificateDomainHome certificateDomainHome;

	@In(value = UserQuery.NAME, create = true)
	private UserQuery userQuery;

	@In(value = UserRepository.NAME, create = true)
	private UserRepository userRepository;

	@In(value = DistinguishedNameManager.NAME, create = true)
	private DistinguishedNameManager distinguishedNameManager;

	@In(value = UserHome.NAME, create = true)
	private UserHome userHome;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registerUser(String userRRN, String userLastName, String userFirstName, Integer domainId, String emailAddress, String locale)
			throws RegistrationException {
		// Check if there are already users in the database; if not this one
		// becomes admin
		boolean isAdmin = 0 == userRepository.getUserCount();

		// Lookup or create the user
		if (StringUtils.isBlank(userRRN)) {
			throw new RegistrationException("Empty user RRN.");
		}
		User user = userRepository.findByNationalRegisterNumber(userRRN);
		if (user == null) {
			user = new User();
			user.setFirstName(userFirstName);
			user.setLastName(userLastName);
			user.setNationalRegisterNumber(userRRN);
			user.setAdmin(isAdmin);
			user.setLocale(locale != null ? locale : "en");
			userRepository.persist(user);
		} else {
			userHome.setInstance(user);
			userHome.getInstance().setLocale(locale);
			userHome.update();
		}

		// Check if we have to add him to a certificate domain
		if (domainId != null) {
			// Lookup the domain
			certificateDomainHome.setId(domainId);
			CertificateDomain domain = certificateDomainHome.find();
			if (domain == null) {
				throw new RegistrationException("Unknown certificate domain.");
			}

			// Validate the e-mail address
			if (StringUtils.isBlank(emailAddress) || !createEmailValidator().isValid(emailAddress)) {
				throw new RegistrationException("Invalid e-mail address");
			}

			// Check if the registration is new
			if (registrationRepository.findRegistration(domain, user) != null) {
				throw new RegistrationException("User already has a registration for this domain.");
			}

			// Create the registration
			Registration registration = new Registration();
			registration.setCertificateDomain(domain);
			registration.setRequester(user);
			registration.setEmail(emailAddress);
			registration.setStatus(RegistrationStatus.NEW);

			registrationRepository.persist(registration);

			if (Events.exists()) {
				Events.instance().raiseEvent(RegistrationMailHandler.REGISTRATION_CREATED, registration);
			}
		}
	}

	@Override
	public boolean createOrUpdateRegistration(RegistrationWS registrationWS) {
		registrationHome.setId(registrationWS.getId() != null ? Integer.valueOf(registrationWS.getId()) : null);
		Registration registration = registrationHome.getInstance();
		registration.setEmail(registrationWS.getUserEmail());
		certificateDomainHome.setId(Integer.valueOf(registrationWS.getCertificateDomainId()));
		registration.setCertificateDomain(certificateDomainHome.find());
		registration.setRequester(userQuery.getFindByUserRRN(registrationWS.getUserRRN()));
		if (registrationHome.isManaged()) {
			return registrationHome.update() != null ? true : false;
		} else {
			registration.setStatus(RegistrationStatus.NEW);
			boolean result = registrationHome.persist() != null;
			if (Events.exists()) {
				Events.instance().raiseEvent(RegistrationMailHandler.REGISTRATION_CREATED, registration);
			}
			return result;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Registration> findRegistrationForUserDNAndCertificateType(String userIdentification, String dn,
			List<String> alternativeNames, CertificateType certificateType) {
		// Parse the DNs
		List<DistinguishedName> theDNs = parseDNs(dn, alternativeNames);
		if (theDNs.size() == 0) {
			return Collections.emptyList();
		}

		// Get all registrations for the user
		List<Registration> allRegistrations = findApprovedRegistrationsByUser(userIdentification);

		// Filter the registrations
		List<Registration> validRegistrations = new ArrayList<Registration>();
		nextRegistration: for (Registration registration : allRegistrations) {
			CertificateDomain certificateDomain = registration.getCertificateDomain();

			// Check certificate types
			if (certificateType != null && !certificateDomain.getCertificateTypes().contains(certificateType)) {
				continue nextRegistration;
			}

			// Check DNs
			DistinguishedNameExpression dnExpression = parseDNExpression(certificateDomain.getDnExpression());
			if (dnExpression == null) {
				continue nextRegistration;
			}
			for (DistinguishedName theDN : theDNs) {
				if (!dnExpression.matches(theDN)) {
					continue nextRegistration;
				}
			}

			// All ok
			validRegistrations.add(registration);
		}

		// Return the result
		return validRegistrations;
	}

	@Override
	public Registration findRegistrationForUserAndCertificateDomain(String userIdentification, CertificateDomain certificateDomain) {
		User user = findUser(userIdentification);
		return registrationRepository.findRegistration(certificateDomain, user);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeLocale(String userRRN, String locale) {
		User user = userHome.findByNationalRegisterNumber(userRRN);
		userHome.setInstance(user);
		userHome.getInstance().setLocale(locale);
		userHome.update();
	}

	private List<Registration> findApprovedRegistrationsByUser(String userIdentification) {
		User user = findUser(userIdentification);
		if (user == null) {
			return Collections.emptyList();
		}

		return registrationRepository.findApprovedRegistrationsByUser(user);
	}

	private User findUser(String userIdentification) {
		User user = userRepository.findByNationalRegisterNumber(userIdentification);
		if (user == null) {
 			user = userRepository.findByCertificateSubject(userIdentification);
		}
		return user;
	}

	private DistinguishedName parseDN(String distinguishedName) {
		DistinguishedName theDN;
		try {
			theDN = distinguishedNameManager.createDistinguishedName(distinguishedName);
		} catch (InvalidDistinguishedNameException e) {
			log.warn("Invalid DN given to checkAuthorizationForUserAndDN: {0}", e, distinguishedName);
			theDN = null;
		}
		return theDN;
	}
	
	private List<DistinguishedName> parseDNs(String dn, List<String> alternativeNames) {
		List<DistinguishedName> theDNs = new ArrayList<DistinguishedName>();

		// Parse the first DN
		DistinguishedName firstDN = parseDN(dn);
		if (firstDN == null) {
			return theDNs;
		}
		theDNs.add(firstDN);

		// Handle the alternative names
		for (String alternativeName : alternativeNames) {
			DistinguishedName theDN = firstDN.replacePart("cn", alternativeName);
			theDNs.add(theDN);
		}

		return theDNs;
	}

	private DistinguishedNameExpression parseDNExpression(String dnExpression) {
		DistinguishedNameExpression theDN;
		try {
			theDN = distinguishedNameManager.createDistinguishedNameExpression(dnExpression);
		} catch (InvalidDistinguishedNameException e) {
			log.warn("Invalid DN expression: {0}", e, dnExpression);
			theDN = null;
		}
		return theDN;
	}

	private EmailValidator createEmailValidator() {
		EmailValidator emailValidator = new EmailValidator();
		emailValidator.initialize(null);
		return emailValidator;
	}

	protected void setRegistrationRepository(RegistrationRepository registrationRepository) {
		this.registrationRepository = registrationRepository;
	}

	protected void setCertificateDomainHome(CertificateDomainHome certificateDomainHome) {
		this.certificateDomainHome = certificateDomainHome;
	}

	protected void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	protected void setLog(Log log) {
		this.log = log;
	}

	protected void setDistinguishedNameManager(DistinguishedNameManager distinguishedNameManager) {
		this.distinguishedNameManager = distinguishedNameManager;
	}

	protected void setRegistrationHome(RegistrationHome registrationHome) {
		this.registrationHome = registrationHome;
	}

	protected void setUserQuery(UserQuery userQuery) {
		this.userQuery = userQuery;
	}

	protected void setUserHome(UserHome userHome) {
		this.userHome = userHome;
	}

}
