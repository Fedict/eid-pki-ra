/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;
import be.fedict.eid.pkira.generated.privatews.RegistrantWS;
import be.fedict.eid.pkira.generated.privatews.RegistrationStatusWS;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;

/**
 * @author Bram Baeyens
 *
 */
@Name(RegistrationMapper.NAME)
@Scope(ScopeType.STATELESS)
public class RegistrationMapperBean implements RegistrationMapper {

	private final class RegistrationWSComparator implements Comparator<RegistrantWS> {

		@Override
		public int compare(RegistrantWS reg1, RegistrantWS reg2) {
			return reg1.getName().compareTo(reg2.getName());
		}

	}
	
	private final ObjectFactory factory;
	
	public RegistrationMapperBean() {
		this.factory = new ObjectFactory();
	}	
	
	@Override
	public List<RegistrationWS> map(List<Registration> registrations) {
		List<RegistrationWS> registrationWSList = new ArrayList<RegistrationWS>();
		for (Registration registration : registrations) {
			registrationWSList.add(map(registration));
		}
		return registrationWSList;
	}

	@Override
	public RegistrationWS map(Registration registration) {
		// Map the registration
		RegistrationWS registrationWS = factory.createRegistrationWS();
		registrationWS.setId(registration.getId().toString());
		registrationWS.setStatus(Enum.valueOf(RegistrationStatusWS.class, registration.getStatus().name()));
		registrationWS.setUserEmail(registration.getEmail());
		
		// Map certificate domain fields
		CertificateDomain certificateDomain = registration.getCertificateDomain();
		registrationWS.setCertificateDomainName(certificateDomain.getName());
		registrationWS.setCertificateDomainId(certificateDomain.getId().toString());
		registrationWS.setCertificateDomainExpression(certificateDomain.getDnExpression());
		
		// Map other registrants if status is approved 
		if (registration.isApproved()) {
			for (Registration other: certificateDomain.getRegistrations()) {
				if (other.isApproved()) {
					registrationWS.getRegistrants().add(mapRegistrant(other));
				}
			}
			Collections.sort(registrationWS.getRegistrants(), new RegistrationWSComparator());
		}
		
		return registrationWS;
	}

	private RegistrantWS mapRegistrant(Registration other) {
		RegistrantWS result = new RegistrantWS();
		result.setEmail(other.getEmail());
		User requester = other.getRequester();
		result.setName(requester.getLastName() + ", " + requester.getFirstName());
		return result;
	}

}
