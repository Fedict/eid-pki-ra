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

package be.fedict.eid.pkira.blm.model.mappers;

import java.util.ArrayList;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;

import static org.testng.Assert.assertEquals;

public class RegistrationMapperBeanTest {

	@Test
	public void mapApprovedRegistration() {
		RegistrationWS registrationWS = new RegistrationMapperBean().map(createRegistration(RegistrationStatus.APPROVED));
		assertEquals(registrationWS.getRegistrants().size(), 1);
	}
	
	@Test
	public void mapNewRegistration() {
		RegistrationWS registrationWS = new RegistrationMapperBean().map(createRegistration(RegistrationStatus.NEW));
		assertEquals(registrationWS.getRegistrants().size(), 0);
	}

	private Registration createRegistration(RegistrationStatus status) {
		CertificateDomain domain = new CertificateDomain();
		domain.setName("domain");
		domain.setDnExpression("expr");
		domain.setRegistrations(new ArrayList<Registration>());
		domain.setId(20);

		User requester = new User();
		requester.setFirstName("first");
		requester.setLastName("last");

		Registration registration = new Registration();
		registration.setCertificateDomain(domain);
		registration.setEmail("email");
		registration.setId(10);
		registration.setRequester(requester);
		registration.setStatus(status);

		domain.getRegistrations().add(registration);
		return registration;
	}
}
