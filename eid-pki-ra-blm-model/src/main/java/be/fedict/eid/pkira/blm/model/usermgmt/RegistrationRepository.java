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

import java.util.List;

import javax.ejb.Local;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;


/**
 * @author Bram Baeyens
 *
 */
@Local
public interface RegistrationRepository {
	
	String NAME = "be.fedict.eid.pkira.blm.registrationRepository";
	
	void persist(Registration registration);
	
	void setDisapproved(Registration registration);
	
	void setApproved(Registration registration);
	
	List<Registration> findAllNewRegistrations();
	
	List<Registration> findApprovedRegistrationsByUser(User user);

	Registration findRegistration(CertificateDomain domain, User user);

	Registration getReference(Integer registrationId);
	
	int getNumberOfRegistrationsForForUserInStatus(User user, RegistrationStatus approved);

	
}
