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

package be.fedict.eid.pkira.authentication;

import java.io.Serializable;

/**
 * @author Bram Baeyens
 */
public class EIdUser implements Serializable {

	private static final long serialVersionUID = -8678403161691396234L;

	private final String rrn;
	private final String firstName;
	private final String lastName;

	public EIdUser(String rrn, String firstName, String lastName) {
		super();
		this.rrn = rrn;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getRRN() {
		return rrn;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getName() {
		return new StringBuilder(firstName).append(' ').append(lastName).toString();
	}

	@Override
	public String toString() {
		return new StringBuilder("EIdUser[").append("rrn=").append(rrn).append(", firstName=").append(firstName)
				.append(", lastName=").append(lastName).append(']').toString();
	}
}
