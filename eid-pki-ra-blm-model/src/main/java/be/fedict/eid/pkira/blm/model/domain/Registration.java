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

package be.fedict.eid.pkira.blm.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;

/**
 * @author Bram Baeyens
 *
 */
@Entity
@Name("registration")
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames={"FK_REQUESTER_ID", "FK_CERTIFICATE_DOMAIN_ID"})
})
@NamedQuery(
		name = "findRegistrationsByStatus",
		query = "SELECT r FROM Registration r WHERE r.status = :status"
)
public class Registration implements Serializable {
	
	private static final long serialVersionUID = -703013819235326427L;
	
	@Id @GeneratedValue
	@Column(name="REGISTRATION_ID")
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(name="REGISTRATION_STATUS")
	private RegistrationStatus status;
	@Email @NotEmpty
	@Column(name="EMAIL", nullable=false)
	private String email;
	@ManyToOne @NotNull
	@JoinColumn(name="FK_REQUESTER_ID",
			referencedColumnName="USER_ID")
	private User requester;
	@ManyToOne @NotNull
	@JoinColumn(name="FK_CERTIFICATE_DOMAIN_ID",
			referencedColumnName="CERTIFICATE_DOMAIN_ID")
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} 
		if (!(obj instanceof Registration)) {
			return false;
		}
		Registration that = (Registration) obj;
		return this.certificateDomain.equals(that.certificateDomain)
				&& this.requester.equals(that.requester);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 43)
				.append(certificateDomain)
				.append(requester)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Registration[")
				.append("id=").append(id)
				.append(", email=").append(email)
				.append(", status=").append(status)
				.append(", requester=").append(requester)
				.append(", certificateDomain=").append(certificateDomain)
				.append(']').toString();				
	}
}
