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

package be.fedict.eid.pkira.blm.model.usermgmt;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotEmpty;

import be.fedict.eid.pkira.blm.model.usermgmt.validation.UniqueUserCertificate;
import be.fedict.eid.pkira.blm.model.usermgmt.validation.UniqueUserNationalRegisterNumber;
import be.fedict.eid.pkira.blm.model.usermgmt.validation.ValidNationalRegisterNumber;
import be.fedict.eid.pkira.blm.model.usermgmt.validation.ValidUserCertificate;

/**
 * @author Bram Baeyens
 */
@Entity
@Table(name = "PKIRAUSER")
@NamedQueries(value =
	{
			@NamedQuery(name = "findByNationalRegisterNumber", query = "SELECT u FROM User u WHERE u.nationalRegisterNumber = :nationalRegisterNumber"),
			@NamedQuery(name = "findByCertificateSubject", query = "SELECT u FROM User u WHERE u.certificateSubject = :certificateSubject"),
			@NamedQuery(name = "getUserCount", query = "SELECT COUNT(u) FROM User u"),
			@NamedQuery(name = "getAdminUsersWithEmail", query = "SELECT u FROM User u WHERE u.admin=true AND u.adminEmail IS NOT NULL") })
public class User implements Serializable {

	private static final long serialVersionUID = -567680538869751475L;

	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private Integer id;
	@Column(name = "LAST_NAME", nullable = false)
	@NotEmpty
	private String lastName;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "NATIONAL_REGISTER_NUMBER", unique = true, nullable = true)
	@UniqueUserNationalRegisterNumber
	@ValidNationalRegisterNumber
	private String nationalRegisterNumber;
	@Column(name = "CERTIFICATE_SUBJECT", nullable = true)
	private String certificateSubject;
	@Lob
	@Column(name = "CERTIFICATE", nullable = true)
	@Basic(fetch = FetchType.LAZY)
	@ValidUserCertificate
	@UniqueUserCertificate
	private String certificate;
	@Column(name = "IS_ADMIN", nullable = false)
	private boolean admin;
	@Column(name = "ADMIN_EMAIL", nullable = true)
	@Email(message = "{validator.email}")
	private String adminEmail;
	@Column(name = "ADMIN_EMAIL_REGISTRATION")
	private Boolean sendRegistrationMail = true;
	@Column(name = "LOCALE", nullable = true)
	private String locale = "en";

	@OneToMany(mappedBy = "requester")
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

    public String getFullName() {
        return firstName + " " + lastName;
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

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getCertificateSubject() {
		return certificateSubject;
	}

	void setCertificateSubject(String certificateSubject) {
		this.certificateSubject = certificateSubject;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public boolean isSendRegistrationMail() {
		return sendRegistrationMail != null ? sendRegistrationMail : true;
	}

	public void setSendRegistrationMail(boolean sendRegistrationMail) {
		this.sendRegistrationMail = sendRegistrationMail;
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
		return "User[" + "nationalRegisterNumber=" + nationalRegisterNumber + ", lastName=" + lastName + ", firstName=" + firstName + ']';
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
