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
	void registerUser(String userRRN, String userLastName, String userFirstName, Integer domainId, String emailAddress)
			throws RegistrationException;
	
	boolean createOrUpdateRegistration(RegistrationWS registrationWS);

	/**
	 * Checks if a user is allowed to register or revoke certificates with the
	 * given DN.
	 * 
	 * @param userRRN
	 *            the RRN of the user to check.
	 * @param distinguishedName
	 *            the DN to check.
	 * @return the registration for this user and certificate domain (null if
	 *         the user is not authorized).
	 */
	Registration findRegistrationForUserDNAndCertificateType(String userRRN, String distinguishedName, CertificateType type);

}
