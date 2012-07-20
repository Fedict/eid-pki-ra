/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;

/**
 * Manager for registrations.
 * 
 * @author Jan Van den Bergh
 */
public interface RegistrationManager {

	String NAME = "be.fedict.pkira.blm.registrationManager";

	/**
	 * Registers a user for a domain.
	 * 
	 * @throws RegistrationException
	 *             if this fails.
	 */
	void registerUser(String userRRN, String userLastName, String userFirstName, Integer domainId, String emailAddress, String locale)
			throws RegistrationException;

	/**
	 * Creates a registration based on an incoming web service call.
	 */
	boolean createOrUpdateRegistration(RegistrationWS registrationWS);

	/**
	 * Checks if a user is allowed to register or revoke certificates with the
	 * given DN.
	 * 
	 * @param userIdentification
	 *            the id (rrn or dn) of the user to check.
	 * @param dn TODO
	 * @param alternativeNames
	 *            alternative DN to check.
	 * @param type
	 *            certificate type to consider (null for all).
	 * @return the registration for this user and certificate domain (null if
	 *         the user is not authorized).
	 */
	List<Registration> findRegistrationForUserDNAndCertificateType(String userIdentification, String dn, List<String> alternativeNames, CertificateType type);

	/**
	 * Checks if a user is allowed to change certificates for this certificate
	 * domain.
	 * 
	 * @param userRRN
	 *            the RRN of the user to check.
	 * @param certificateDomain
	 *            the certificate domain to check.
	 * @return the registration for this user and certificate domain (null if
	 *         the user is not authorized).
	 */
	Registration findRegistrationForUserAndCertificateDomain(String signer, CertificateDomain certificateDomain);

	/**
	 * Change the locale of the user.
	 */
	void changeLocale(String userRRN, String locale);

}
