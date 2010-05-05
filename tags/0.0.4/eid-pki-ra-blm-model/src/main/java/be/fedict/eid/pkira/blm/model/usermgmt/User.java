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

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Bram Baeyens
 */
@Entity
@NamedQueries(value={
		@NamedQuery(
				name = "findByNationalRegisterNumber", 
				query = "SELECT u FROM User u WHERE u.nationalRegisterNumber = :nationalRegisterNumber"),
		@NamedQuery(
				name = "getUserCount",
				query = "SELECT COUNT(u) FROM User u"
		)
})
public class User implements Serializable {

	private static final long serialVersionUID = -567680538869751475L;

	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private Integer id;
	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "NATIONAL_REGISTER_NUMBER", unique = true, nullable = false)
	private String nationalRegisterNumber;
	@Column(name = "IS_ADMIN", nullable=false)
	private boolean admin;
	
	@OneToMany(mappedBy="requester")
	private List<Registration> registrations;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getNationalRegisterNumber() {
		return nationalRegisterNumber;
	}

	public void setNationalRegisterNumber(String nationalRegisterNumber) {
		this.nationalRegisterNumber = nationalRegisterNumber;
	}

	public String getName() {
		return new StringBuilder(firstName).append(' ').append(lastName).toString();
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isAdmin() {
		return admin;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User that = (User) obj;
		return this.nationalRegisterNumber.equals(that.nationalRegisterNumber);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(13, 97).append(nationalRegisterNumber).toHashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder("User[").append("nationalRegisterNumber=").append(nationalRegisterNumber).append(
				", lastName=").append(lastName).append(", firstName=").append(firstName).append(']').toString();
	}


}
