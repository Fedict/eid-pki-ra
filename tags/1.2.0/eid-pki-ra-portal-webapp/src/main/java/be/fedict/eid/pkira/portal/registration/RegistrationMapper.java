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

package be.fedict.eid.pkira.portal.registration;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;

/**
 * @author Bram Baeyens
 *
 */
@Name(RegistrationMapper.NAME)
@Scope(ScopeType.STATELESS)
public class RegistrationMapper implements Serializable {

	private static final long serialVersionUID = 28232217429897167L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.registrationMapper";

    @In(EIdUserCredentials.NAME)
	private EIdUserCredentials credentials;

	public Registration map(RegistrationWS registrationWS) {
		Registration registration = new Registration();
		registration.setId(registrationWS.getId());
		registration.setStatus(Enum.valueOf(RegistrationStatus.class, registrationWS.getStatus().name()));		
		registration.setEmailAddress(registrationWS.getUserEmail());
		registration.setEmailAddressVerification(registrationWS.getUserEmail());
		registration.setCertificateDomainId(registrationWS.getCertificateDomainId());
		registration.setCertificateDomainName(registrationWS.getCertificateDomainName());
		registration.setCertificateDomainExpression(registrationWS.getCertificateDomainExpression());
		registration.setRegistrants(registrationWS.getRegistrants());
		return registration;
	}

	public RegistrationWS map(Registration registration) {
		RegistrationWS registrationWS = new ObjectFactory().createRegistrationWS();
		registrationWS.setCertificateDomainId(registration.getCertificateDomainId());
		registrationWS.setId(registration.getId());
		registrationWS.setUserEmail(registration.getEmailAddress());
		registrationWS.setUserRRN(credentials.getUser().getRRN());
		return registrationWS;
	}
}
