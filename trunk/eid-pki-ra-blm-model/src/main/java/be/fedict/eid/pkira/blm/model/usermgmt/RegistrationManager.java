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

/**
 * Manager for registrations.
 * 
 * @author Jan Van den Bergh
 */
public interface RegistrationManager {

	String NAME = "be.fedict.pkira.blm.registrationManager";

	/**
	 * Registers a user for a domain.
	 * @throws RegistrationException
	 *             if this fails.
	 */
	void registerUser(String userRRN, String userLastName, String userFirstName, int domainId, String emailAddress)
			throws RegistrationException;
	
	/**
	 * Approve the registration.
	 * @param integer the ID of the registration.
	 * @param reasonText reason to approve the registration.
	 */
	void approveRegistration(Integer integer, String reasonText);
	
	/**
	 * Disapprove the registration.
	 * @param integer the ID of the registration.
	 * @param reasonText reason to disapprove the registration.
	 */
	void disapproveRegistration(Integer integer, String reasonText);

}
