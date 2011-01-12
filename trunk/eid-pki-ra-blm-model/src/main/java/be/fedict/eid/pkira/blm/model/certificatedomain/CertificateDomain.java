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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.NotEmpty;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.ca.CertificateAuthority;
import be.fedict.eid.pkira.blm.model.certificatedomain.validation.UniqueCertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.validation.UniqueCertificateDomainName;
import be.fedict.eid.pkira.blm.model.certificatedomain.validation.ValidCertificateDomainDnExpression;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;

/**
 * @author Bram Baeyens
 */
@Entity
@Table(name="CERTIFICATE_DOMAIN")
@NamedQueries(
	{
			@NamedQuery(name = "findCertificateDomainByName", query = "FROM CertificateDomain WHERE name = :name"),
			@NamedQuery(name = "findCertificateDomainByDnExpression", query = "FROM CertificateDomain WHERE dnExpression = :dnExpression"),
			@NamedQuery(name = "findCertificateDomainUnregistered", query = "FROM CertificateDomain cd WHERE cd NOT IN (SELECT r.certificateDomain FROM Registration r WHERE r.requester = :requester) ORDER BY cd.name"),
			@NamedQuery(name = "findCertificateDomainByCertificateTypes", query = "FROM CertificateDomain WHERE (clientCertificate=:forClient OR :forClient=FALSE) AND (serverCertificate=:forServer OR :forServer=FALSE) AND (codeSigningCertificate=:forCode OR :forCode=FALSE)") })
@Name(CertificateDomain.NAME)
@UniqueCertificateDomain
public class CertificateDomain implements Serializable {

	private static final long serialVersionUID = -4193917177011312256L;

	public static final String NAME = "be.fedict.eid.pkira.blm.CertificateDomain";

	@Id
	@GeneratedValue
	@Column(name = "CERTIFICATE_DOMAIN_ID")
	private Integer id;
	@Column(name = "CERTIFICATE_DOMAIN_NAME", nullable = false, unique = true)
	@NotEmpty(message = "{validation.empty.certificateDomainName}")
	@UniqueCertificateDomainName
	private String name;

	@ManyToOne(optional = false)
	@JoinColumn(name = "CA_ID", nullable = false)
	private CertificateAuthority certificateAuthority;

	@Column(name = "DN_EXPRESSION", nullable = false)
	@NotEmpty(message = "{validation.empty.dnExpression}")
	@ValidCertificateDomainDnExpression
	private String dnExpression;
	@Column(name = "SERVERCERT", nullable = false)
	private boolean serverCertificate;
	@Column(name = "CLIENTCERT", nullable = false)
	private boolean clientCertificate;
	@Column(name = "CODECERT", nullable = false)
	private boolean codeSigningCertificate;
	@Column(name = "PERSONALCERT", nullable = false)
	private boolean personalCertificate;

	@OneToMany(mappedBy = "certificateDomain")
	private List<Registration> registrations;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDnExpression() {
		return dnExpression;
	}

	public void setDnExpression(String dnExpression) {
		this.dnExpression = dnExpression;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CertificateDomain)) {
			return false;
		}
		CertificateDomain that = (CertificateDomain) obj;
		return this.id == null ? false : this.id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 43).append(id).toHashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder("CertificateDomain[").append("name=").append(name).append(", dnExpression=").append(
				dnExpression).append(']').toString();
	}

	public boolean isServerCertificate() {
		return serverCertificate;
	}

	public void setServerCertificate(boolean serverCertificate) {
		this.serverCertificate = serverCertificate;
	}

	public boolean isClientCertificate() {
		return clientCertificate;
	}

	public void setClientCertificate(boolean clientCertificate) {
		this.clientCertificate = clientCertificate;
	}
	
	public boolean isPersonalCertificate() {
		return personalCertificate;
	}

	public void setPersonalCertificate(boolean personalCertificate) {
		this.personalCertificate = personalCertificate;
	}

	public boolean isCodeSigningCertificate() {
		return codeSigningCertificate;
	}

	public void setCodeSigningCertificate(boolean codeSigningCertificate) {
		this.codeSigningCertificate = codeSigningCertificate;
	}

	@NotEmpty(message = "{validation.empty.certificateTypes}")
	public Set<CertificateType> getCertificateTypes() {
		Set<CertificateType> result = new HashSet<CertificateType>();
		if (clientCertificate) {
			result.add(CertificateType.CLIENT);
		}
		if (serverCertificate) {
			result.add(CertificateType.SERVER);
		}
		if (codeSigningCertificate) {
			result.add(CertificateType.CODE);
		}
		if (personalCertificate) {
			result.add(CertificateType.PERSONAL);
		}
		return result;
	}

	public CertificateAuthority getCertificateAuthority() {
		return certificateAuthority;
	}

	public void setCertificateAuthority(CertificateAuthority certificateAuthority) {
		this.certificateAuthority = certificateAuthority;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}
}
