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

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Bram Baeyens
 */
@Name("signableCertificate")
@Scope(ScopeType.CONVERSATION)
public class CertificateSigningRequest extends SignableCertificate {

	private static final long serialVersionUID = 4134577004436570408L;
	
	private int validityPeriod = 15;

	public int getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	@Override
	protected String getDssSignatureHttpRequestHandlerPath() {
		return "/seam/resource/requestCertificateResource";
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new StringBuilder("CertificateSigningRequest[")
			.append(super.toString())
			.append(", validityPeriod=").append(validityPeriod)
			.append(']').toString();
	}
}
