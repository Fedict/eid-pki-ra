/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.certificatedomain.handler;

import java.io.Serializable;

import org.hibernate.validator.NotEmpty;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;

/**
 * @author Jan Van den Bergh
 */
@Name(CertificateDomainItem.NAME)
public class CertificateDomainItem implements Serializable {

	private static final long serialVersionUID = -4533238737577494662L;

	public static final String NAME = "be.fedict.eid.pkira.blm.certificateDomainItem";

	@NotEmpty
	private String name;
	
	@NotEmpty
	private String dnExpression;
	
	private boolean clientCertificate, serverCertificate, codeSigningCertificate;

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

	public boolean isClientCertificate() {
		return clientCertificate;
	}

	public void setClientCertificate(boolean clientCertificate) {
		this.clientCertificate = clientCertificate;
	}

	public boolean isServerCertificate() {
		return serverCertificate;
	}

	public void setServerCertificate(boolean serverCertificate) {
		this.serverCertificate = serverCertificate;
	}

	public boolean isCodeSigningCertificate() {
		return codeSigningCertificate;
	}

	public void setCodeSigningCertificate(boolean codeSigningCertificate) {
		this.codeSigningCertificate = codeSigningCertificate;
	}

	public CertificateDomain toCertificateDomain() {
		CertificateDomain result = new CertificateDomain();
		result.setName(name);
		result.setDnExpression(dnExpression);
		result.setClientCertificate(clientCertificate);
		result.setServerCertificate(serverCertificate);
		result.setCodeSigningCertificate(codeSigningCertificate);
		
		return result;
	}
}
