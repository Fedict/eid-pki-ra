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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registerUser(String userRRN, String userLastName, String userFirstName, Integer domainId,
			String emailAddress) throws RegistrationException {
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
			userRepository.persist(user);
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
	public Registration findRegistrationForUserDNAndCertificateType(String userIdentification, String dn,
			List<String> names, CertificateType type) {
		// Parse the DN
		DistinguishedName theDN = parseDN(dn);
		if (theDN == null) {
			return null;
		}

		// Get the user and his registrations
		User user = userRepository.findByNationalRegisterNumber(userIdentification);
		if (user == null) {
			user = userRepository.findByCertificateSubject(userIdentification);
		}
		if (user == null) {
			return null;
		}

		List<Registration> activeRegistrations = registrationRepository.findApprovedRegistrationsByUser(user);
		if (activeRegistrations == null || activeRegistrations.size() == 0) {
			return null;
		}

		// See if the first DN matches a registration
		Registration registration = findRegistrationForDN(type, theDN, activeRegistrations);
		if (registration==null) {
			return null;
		}

		// Check the alternative names
		DistinguishedNameExpression dnExpr = parseDNameExpression(registration.getCertificateDomain().getDnExpression());
		for (String name : names) {
			DistinguishedName otherDN = theDN.replacePart("cn", name);
			if (!dnExpr.matches(otherDN)) {
				return null;
			}
		}

		// Nothing found
		return registration;
	}

	@Override
	public Registration findRegistrationForUserAndCertificateDomain(String signer, CertificateDomain certificateDomain) {
		User user = userRepository.findByNationalRegisterNumber(signer);
		return registrationRepository.findRegistration(certificateDomain, user);
	}

	private Registration findRegistrationForDN(CertificateType type, DistinguishedName theDN,
			List<Registration> activeRegistrations) {
		for (Registration registration : activeRegistrations) {
			CertificateDomain certificateDomain = registration.getCertificateDomain();
			if (!certificateDomain.getCertificateTypes().contains(type)) {
				continue;
			}

			String dnExpression = certificateDomain.getDnExpression();
			DistinguishedNameExpression domainDN = parseDNameExpression(dnExpression);
			if (domainDN == null) {
				throw new RuntimeException("Invalid certificate domain in database: " + dnExpression);
			}

			if (domainDN.matches(theDN)) {
				return registration;
			}
		}

		return null;
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

	private DistinguishedNameExpression parseDNameExpression(String distinguishedName) {
		DistinguishedNameExpression theDN;
		try {
			theDN = distinguishedNameManager.createDistinguishedNameExpression(distinguishedName);
		} catch (InvalidDistinguishedNameException e) {
			log.warn("Invalid DN given to checkAuthorizationForUserAndDN: {0}", e, distinguishedName);
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
}
