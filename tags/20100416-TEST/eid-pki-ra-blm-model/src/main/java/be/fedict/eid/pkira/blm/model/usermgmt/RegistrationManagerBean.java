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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.EmailValidator;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;
import be.fedict.eid.pkira.dnfilter.DistinguishedName;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

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

	@In(value = CertificateDomainHome.NAME, create = true)
	private CertificateDomainHome certificateDomainHome;

	@In(value = UserRepository.NAME, create = true)
	private UserRepository userRepository;

	@In(value = MailTemplate.NAME, create = true)
	private MailTemplate mailTemplate;

	@In(value = DistinguishedNameManager.NAME, create = true)
	private DistinguishedNameManager distinguishedNameManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registerUser(String userRRN, String userLastName, String userFirstName, int domainId,
			String emailAddress) throws RegistrationException {
		// Lookup the domain
		certificateDomainHome.setId(domainId);
		CertificateDomain domain = certificateDomainHome.find();
		if (domain == null) {
			throw new RegistrationException("Unknown certificate domain.");
		}

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
			userRepository.persist(user);
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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void approveRegistration(Integer registrationId, String reasonText) {
		// Mark as approved
		Registration registration = registrationRepository.getReference(registrationId);
		registrationRepository.setApproved(registration);

		// Send the mail
		String[] recipients = new String[]
			{ registration.getEmail() };
		Map<String, Object> parameters = createMapForRegistrationMail(registration, reasonText);
		mailTemplate.sendTemplatedMail("registrationApproved.ftl", parameters, recipients);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void disapproveRegistration(Integer registrationId, String reasonText) {
		// Mark as disapproved
		Registration registration = registrationRepository.getReference(registrationId);
		registrationRepository.setDisapproved(registration);

		// Send the mail
		String[] recipients = new String[]
			{ registration.getEmail() };
		Map<String, Object> parameters = createMapForRegistrationMail(registration, reasonText);
		mailTemplate.sendTemplatedMail("registrationDisapproved.ftl", parameters, recipients);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Registration findRegistrationForUserDNAndCertificateType(String userRRN, String distinguishedName, CertificateType type) {
		// Parse the DN
		DistinguishedName theDN = parseDN(distinguishedName);
		if (theDN==null) {
			return null;
		}
		
		// Get the certificate domains for the user		
		User user = userRepository.findByNationalRegisterNumber(userRRN);
		if (user==null) {
			return null;
		}
		
		List<Registration> activeRegistrations = registrationRepository.findApprovedRegistrationsByUser(user);
		if (activeRegistrations==null || activeRegistrations.size()==0) {
			return null;
		}
		
		// See if one of them matches the DN		
		for (Registration registration: activeRegistrations) {			
			CertificateDomain certificateDomain = registration.getCertificateDomain();
			if (!certificateDomain.getCertificateTypes().contains(type)) {
				continue;
			}
			
			String dnExpression = certificateDomain.getDnExpression();
			DistinguishedName domainDN = parseDN(dnExpression);
			if (domainDN==null) {
				throw new RuntimeException("Invalid certificate domain in database: " + dnExpression);
			}
			
			if (domainDN.matches(theDN)) {
				return registration;
			}
		}

		// Nothing found
		return null;
	}

	private DistinguishedName parseDN(String distinguishedName) {
		DistinguishedName theDN;
		try {
			theDN = distinguishedNameManager.createDistinguishedName(distinguishedName);
		} catch (InvalidDistinguishedNameException e) {
			log.warn("Invalid DN given to checkAuthorizationForUserAndDN: {0}", e, distinguishedName);
			theDN=null;
		}
		return theDN;
	}

	private Map<String, Object> createMapForRegistrationMail(Registration registration, String reasonText) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("user", registration.getRequester());
		result.put("certificateDomain", registration.getCertificateDomain());
		result.put("reason", reasonText);

		return result;
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

	protected void setMailTemplate(MailTemplate mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	protected void setDistinguishedNameManager(DistinguishedNameManager distinguishedNameManager) {
		this.distinguishedNameManager = distinguishedNameManager;
	}
}
