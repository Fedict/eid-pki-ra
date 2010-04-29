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

package be.fedict.eid.pkira.blm.model.usermgmt;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;

import be.fedict.eid.pkira.blm.model.framework.DataTableEntityQuery;

/**
 * @author Bram Baeyens
 *
 */
@Name(RegistrationQuery.NAME)
public class RegistrationQuery extends DataTableEntityQuery<Registration> {

	private static final long serialVersionUID = 636702321975305314L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.registrationQuery";
	
	private User requester;

	public String getEjbql() {
		return "select registration from Registration registration";
	}
	
	public List<Registration> getResultList() {
		setOrderColumn("registration.certificateDomain.name");
		return super.getResultList();
	}
	
	@Observer("org.jboss.seam.afterTransactionSuccess.Registration")
	public void refresh() {
		super.refresh();
	}
	
	public List<Registration> findByUserRRN(String userRRN) {
		requester = new User();
		requester.setNationalRegisterNumber(userRRN);
		setRestrictionExpressionStrings(Arrays.asList(
				new String[] {
						"registration.requester.nationalRegisterNumber = #{registrationQuery.requester.nationalRegisterNumber}"}));
		return getResultList();
	}
	
	public boolean isContainsNewRegistrations() {
		for (Registration registration : super.getResultList()) {
			if (registration.isNew()) {
				return true;
			}
		}
		return false;
	}
	
	public User getRequester() {
		return requester;
	}
}
