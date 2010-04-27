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

package be.fedict.eid.pkira.blm.model.contracts;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractQuery.NAME)
public class ContractQuery extends EntityQuery<AbstractContract> {

	private static final long serialVersionUID = -1687988818281539366L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.contractQuery" ;
	
	private Registration registration;
	
	@In(value = CertificateDomainHome.NAME, create = true)
	private CertificateDomainHome certificateDomainHome;
	
	public String getEjbql() {
		return "select contract from AbstractContract contract, Registration registration";
	}
	
	public List<AbstractContract> getFindContracts(Integer certificateDomainId, String userRrn) {
		setOrder("contract.creationDate");
		setOrderDirection("desc");
		registration = new Registration();
		registration.setRequester(new User());
		registration.getRequester().setNationalRegisterNumber(userRrn);		
		registration.setStatus(RegistrationStatus.APPROVED);		
		certificateDomainHome.setId(certificateDomainId);
		
		setRestrictionExpressionStrings(Arrays.asList(
				new String[] {
						"registration.requester.nationalRegisterNumber = #{registration.requester.nationalRegisterNumber}", 
						"registration.status = #{registration.status}", 
						"registration.certificateDomain = #{certificateDomainHome.instance}", 
						"contract.certificateDomain = #{certificateDomainHome.instance}"}));
		return getResultList();
	}
}
