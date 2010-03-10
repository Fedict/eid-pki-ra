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

package be.fedict.eid.pkira.portal.domain;

import java.io.Serializable;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Credentials;

/**
 * @author Bram Baeyens
 *
 */
@Name("operator")
public class Operator implements Serializable {

	private static final long serialVersionUID = -9069139272251459619L;

	@In 
	Credentials credentials;
	
	private String name = "tester";
	private String function;
	private String phone;
	private String email;
	
	public String getName() {
		if (name == null) {
			name = credentials.getUsername();
		}
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFunction() {
		return function;
	}
	
	public void setFunction(String function) {
		this.function = function;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	} 
	
	public String toString() {
		return new StringBuilder("Operator[")
				.append("name=").append(name)
				.append(", function=").append(function)
				.append(", phone=").append(phone)
				.append(", email=").append(email)
				.append(']').toString();
	}
}
