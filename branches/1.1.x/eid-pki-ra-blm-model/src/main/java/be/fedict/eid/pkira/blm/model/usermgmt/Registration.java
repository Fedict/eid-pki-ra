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

import javax.persistence.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;

/**
 * @author Bram Baeyens
 */
@Entity
@Table(
		name="REGISTRATION", 
		uniqueConstraints =	{ 
				@UniqueConstraint(columnNames = { "FK_REQUESTER_ID", "FK_CERTIFICATE_DOMAIN_ID" }) 
		})
@NamedQueries(
	{
			@NamedQuery(
					name = "findRegistrationsByStatus", 
					query = "FROM Registration r WHERE r.status = :status"),
			@NamedQuery(
					name = "findRegistrationByCertificateDomainAndRequester", 
					query = "FROM Registration r WHERE r.certificateDomain = :certificateDomain AND r.requester=:requester"),
			@NamedQuery(
					name = "findRegistrationsByCertificateDomain", 
					query = "FROM Registration r WHERE r.status=Active and r.certificateDomain=:certificateDomain"), 
			@NamedQuery(
					name = "getNumberOfRegistrationsByUserAndStatus", 
					query = "SELECT count(*) FROM Registration r WHERE r.requester=:user AND r.status=:status"),
			@NamedQuery(
					name = "getRegistrationsByUserAndStatus", 
					query = "FROM Registration r WHERE r.requester=:user AND r.status=:status") })
@Name(Registration.NAME)
public class Registration implements Serializable {

	private static final long serialVersionUID = -703013819235326427L;

	public static final String NAME = "be.fedict.eid.pkira.blm.Registration";
	
	@Id
	@GeneratedValue
	@Column(name = "REGISTRATION_ID")
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(name = "REGISTRATION_STATUS")
	private RegistrationStatus status;
	@Email
	@NotEmpty
	@Column(name = "EMAIL", nullable = false)
	private String email;
	@ManyToOne
	@NotNull
	@JoinColumn(name = "FK_REQUESTER_ID", referencedColumnName = "USER_ID")
	private User requester;
	@ManyToOne
	@NotNull
	@JoinColumn(name = "FK_CERTIFICATE_DOMAIN_ID", referencedColumnName = "CERTIFICATE_DOMAIN_ID")
	private CertificateDomain certificateDomain;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RegistrationStatus getStatus() {
		return status;
	}

	public void setStatus(RegistrationStatus status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public CertificateDomain getCertificateDomain() {
		return certificateDomain;
	}

	public void setCertificateDomain(CertificateDomain certificateDomain) {
		this.certificateDomain = certificateDomain;
	}
	
	public boolean isNew() {
		return RegistrationStatus.NEW == status;
	}
	
	public boolean isApproved() {
		return RegistrationStatus.APPROVED == status;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Registration)) {
			return false;
		}
		Registration that = (Registration) obj;
		return this.certificateDomain.equals(that.certificateDomain) && this.requester.equals(that.requester);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 43).append(certificateDomain).append(requester).toHashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder("Registration[").append("id=").append(id).append(", email=").append(email).append(
				", status=").append(status).append(", requester=").append(requester).append(", certificateDomain=")
				.append(certificateDomain).append(']').toString();
	}
}
