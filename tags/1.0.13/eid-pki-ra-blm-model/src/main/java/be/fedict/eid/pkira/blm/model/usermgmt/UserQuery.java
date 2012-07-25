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

package be.fedict.eid.pkira.blm.model.usermgmt;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.framework.DataTableEntityQuery;

/**
 * @author Bram Baeyens
 *
 */
@Name(UserQuery.NAME)
public class UserQuery extends DataTableEntityQuery<User> {

	private static final long serialVersionUID = -8523995221764583699L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.userQuery";
	
	private User user;
	
	@Override
	public String getEjbql() {
		return "select user from User user";
	}
	
	@Override
	public List<User> getResultList() {
		setOrderColumn("user.lastName");
		return super.getResultList();
	}
	
	public User getFindByUserRRN(String userRRN) {
		user = new User();
		user.setNationalRegisterNumber(userRRN);
		setRestrictionExpressionStrings(Arrays.asList(
				new String[] {
						"user.nationalRegisterNumber = #{userQuery.user.nationalRegisterNumber}"}));
		return super.getSingleResult();
	}
	
	public User getUser() {
		return user;
	}
	
}
