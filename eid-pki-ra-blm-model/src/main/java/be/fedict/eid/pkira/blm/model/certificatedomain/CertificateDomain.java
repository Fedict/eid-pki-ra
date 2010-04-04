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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracts.CertificateType;

/**
 * @author Bram Baeyens
 */
@Entity
@NamedQueries(
	{
			@NamedQuery(name = "findCertificateDomainByName", query = "FROM CertificateDomain WHERE name = :name"),
			@NamedQuery(name = "findCertificateDomainByDnExpression", query = "FROM CertificateDomain WHERE dnExpression = :dnExpression"),
			@NamedQuery(name = "findCertificateDomainUnregistered", query = "FROM CertificateDomain cd WHERE cd NOT IN (SELECT r.certificateDomain FROM Registration r WHERE r.requester = :requester) ORDER BY cd.name"),
			@NamedQuery(name = "findCertificateDomainByCertificateTypes", query = "FROM CertificateDomain WHERE (clientCertificate=:forClient OR :forClient=FALSE) AND (serverCertificate=:forServer OR :forServer=FALSE) AND (codeSigningCertificate=:forCode OR :forCode=FALSE)")
	})
@Name(CertificateDomain.NAME)
public class CertificateDomain implements Serializable {

	private static final long serialVersionUID = -4193917177011312256L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.CertificateDomain";
	
	@Id
	@GeneratedValue
	@Column(name = "CERTIFICATE_DOMAIN_ID")
	private Integer id;
	@Column(name = "CERTIFICATE_DOMAIN_NAME", nullable = false, unique = true)
	private String name;
	@Column(name = "DN_EXPRESSION", nullable = false)
	private String dnExpression;
	@Column(name = "SERVERCERT", nullable = false)
	private boolean serverCertificate;
	@Column(name = "CLIENTCERT", nullable = false)
	private boolean clientCertificate;
	@Column(name = "CODECERT", nullable = false)
	private boolean codeSigningCertificate;

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

	public boolean isCodeSigningCertificate() {
		return codeSigningCertificate;
	}

	public void setCodeSigningCertificate(boolean codeSigningCertificate) {
		this.codeSigningCertificate = codeSigningCertificate;
	}

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
		return result;
	}
}
