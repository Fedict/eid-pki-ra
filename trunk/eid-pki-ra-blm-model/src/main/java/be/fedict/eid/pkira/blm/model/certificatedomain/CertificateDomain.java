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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jboss.seam.annotations.Name;

/**
 * @author Bram Baeyens
 */
@Entity
@Name("certificateDomain")
@NamedQueries(
	{
			@NamedQuery(name = "findCertificateDomainByName", query = "SELECT cd " + "FROM CertificateDomain cd "
					+ "WHERE cd.name = :name"),
			@NamedQuery(name = "findCertificateDomainByDnExpression", query = "SELECT cd " + "FROM CertificateDomain cd "
					+ "WHERE cd.dnExpression = :dnExpression"),
			@NamedQuery(name = "findCertificateDomainUnregistered", query = "SELECT cd " + "FROM CertificateDomain cd "
					+ "WHERE cd NOT IN (" + "SELECT r.certificateDomain " + "FROM Registration r "
					+ "WHERE r.requester = :requester" + ") " + "ORDER BY cd.name")

	})
public class CertificateDomain implements Serializable {

	private static final long serialVersionUID = -4193917177011312256L;

	@Id
	@GeneratedValue
	@Column(name = "CERTIFICATE_DOMAIN_ID")
	private Integer id;
	@Column(name = "CERTIFICATE_DOMAIN_NAME", nullable = false, unique = true)
	private String name;
	@Column(name = "DN_EXPRESSION", nullable = false, unique = true)
	private String dnExpression;
	@Column(name = "SERVERCERT", nullable = false)
	private boolean forServerCertificate;
	@Column(name = "CLIENTCERT", nullable = false)
	private boolean forClientCertificate;
	@Column(name = "CODECERT", nullable = false)
	private boolean forCodeSigningCertificate;

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
		return this.dnExpression.equals(that.dnExpression);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 43).append(dnExpression).toHashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder("CertificateDomain[").append("name=").append(name).append(", dnExpression=").append(
				dnExpression).append(']').toString();
	}

	public boolean isForServerCertificate() {
		return forServerCertificate;
	}

	public void setForServerCertificate(boolean forServerCertificate) {
		this.forServerCertificate = forServerCertificate;
	}

	public boolean isForClientCertificate() {
		return forClientCertificate;
	}

	public void setForClientCertificate(boolean forClientCertificate) {
		this.forClientCertificate = forClientCertificate;
	}

	public boolean isForCodeSigningCertificate() {
		return forCodeSigningCertificate;
	}

	public void setForCodeSigningCertificate(boolean forCodeSigningCertificate) {
		this.forCodeSigningCertificate = forCodeSigningCertificate;
	}

}
