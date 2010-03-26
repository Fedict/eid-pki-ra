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
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.EmailValidator;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainRepository;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;

/**
 * Registration manager implementation.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(RegistrationManager.NAME)
public class RegistrationManagerBean implements RegistrationManager {

	@In(value = RegistrationRepository.NAME, create = true)
	private RegistrationRepository registrationRepository;

	@In(value = CertificateDomainRepository.NAME, create = true)
	private CertificateDomainRepository certificateDomainRepository;

	@In(value = UserRepository.NAME, create = true)
	private UserRepository userRepository;

	@In(value = MailTemplate.NAME, create = true)
	private MailTemplate mailTemplate;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registerUser(String userRRN, String userLastName, String userFirstName, int domainId,
			String emailAddress) throws RegistrationException {
		// Lookup the domain
		CertificateDomain domain = certificateDomainRepository.findById(domainId);
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
		String[] recipients = new String[] { registration.getEmail() };
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
		String[] recipients = new String[] { registration.getEmail() };
		Map<String, Object> parameters = createMapForRegistrationMail(registration, reasonText);
		mailTemplate.sendTemplatedMail("registrationDisapproved.ftl", parameters, recipients);
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

	protected void setCertificateDomainRepository(CertificateDomainRepository certificateDomainRepository) {
		this.certificateDomainRepository = certificateDomainRepository;
	}

	protected void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
