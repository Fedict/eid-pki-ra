/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.generated.privatews.ObjectFactory;
import be.fedict.eid.pkira.generated.privatews.RegistrationStatusWS;
import be.fedict.eid.pkira.generated.privatews.RegistrationWS;

/**
 * @author Bram Baeyens
 *
 */
@Name(RegistrationMapper.NAME)
@Scope(ScopeType.STATELESS)
public class RegistrationMapperBean implements RegistrationMapper {

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
		RegistrationWS registrationWS = factory.createRegistrationWS();
		registrationWS.setId(registration.getId().toString());
		registrationWS.setStatus(Enum.valueOf(RegistrationStatusWS.class, registration.getStatus().name()));
		registrationWS.setUserEmail(registration.getEmail());
		
		CertificateDomain certificateDomain = registration.getCertificateDomain();
		registrationWS.setCertificateDomainName(certificateDomain.getName());
		registrationWS.setCertificateDomainId(certificateDomain.getId().toString());
		registrationWS.setCertificateDomainExpression(certificateDomain.getDnExpression());
		
		return registrationWS;
	}

}
