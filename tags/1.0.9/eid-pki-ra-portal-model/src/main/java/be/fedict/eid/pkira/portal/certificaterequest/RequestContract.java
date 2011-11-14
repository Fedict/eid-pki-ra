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

package be.fedict.eid.pkira.portal.certificaterequest;

import java.util.List;

import org.hibernate.validator.Length;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.portal.certificate.CertificateType;
import be.fedict.eid.pkira.portal.contract.AbstractContract;

/**
 * @author Bram Baeyens
 *
 */
@Name(RequestContract.NAME)
@Scope(ScopeType.CONVERSATION)
public class RequestContract extends AbstractContract {
	
	private static final long serialVersionUID = 8678222804344884153L;

	public static final String NAME = "be.fedict.eid.pkira.portal.requestContract";
	
	private String distinguishedName;
	private List<String> alternativeNames;
	private String base64Csr;
	private int validityPeriod = 15;	
	private CertificateType certificateType;
	
	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	@Length(max=2048, message="{validator.length}")
	public String getBase64Csr() {
		return base64Csr;
	}

	public void setBase64Csr(String base64Csr) {
		this.base64Csr = base64Csr;
	}

	public int getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	
	/**
	 * @param certificateType the certificateType to set
	 */
	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}

	/**
	 * @return the certificateType
	 */
	public CertificateType getCertificateType() {
		return certificateType;
	}

	
	public List<String> getAlternativeNames() {
		return alternativeNames;
	}

	
	public void setAlternativeNames(List<String> alternativeNames) {
		this.alternativeNames = alternativeNames;
	}
	
	public String getAlternativeNamesAsString() {
		StringBuilder builder = new StringBuilder();
		for(String alternativeName: alternativeNames) {
			if (builder.length()!=0) {
				builder.append(" / ");
			}
			builder.append(alternativeName);
		}
		
		return builder.toString();
	}

	@Override
	public String toString() {
		return new StringBuilder("RequestContract[")
				.append(super.toString())
				.append(", distinguishedName").append(distinguishedName)
				.append(", validityPeriod").append(validityPeriod)
				.append(']').toString();				
	}	
}
