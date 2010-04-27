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
 
package be.fedict.eid.pkira.blm.model.certificatedomain;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

@Name(value=RegisteredCertificateDomainQuery.NAME)
public class RegisteredCertificateDomainQuery  extends EntityQuery<CertificateDomain> {

	private static final long serialVersionUID = -7747880164038084896L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.registeredCertificateDomainQuery";
	
	private User user;
	
	private Registration registration;
	
	@Override
	public String getEjbql() {
		return "SELECT certificateDomain FROM CertificateDomain certificateDomain INNER JOIN certificateDomain.registrations registration";
	}
	
	@Override
	public List<CertificateDomain> getResultList() {
		setOrderColumn("certificateDomain.name");
		return super.getResultList();
	}
	
	public List<CertificateDomain> getFindRegisteredCertificateDomains(String userRRN){
		setUser(new User());
		getUser().setNationalRegisterNumber(userRRN);
		
		registration = new Registration();
		registration.setStatus(RegistrationStatus.APPROVED);
		
		setRestrictionExpressionStrings(Arrays.asList(
				new String[] {
						"registration.requester.nationalRegisterNumber = #{certificateDomainQuery.user.nationalRegisterNumber}",
						"registration.status = #{registeredCertificateDomainQuery.registration.status}"}));
		
		return getResultList();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

	public Registration getRegistration() {
		return registration;
	}

}
