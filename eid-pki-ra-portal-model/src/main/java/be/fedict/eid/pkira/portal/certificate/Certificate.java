/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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

package be.fedict.eid.pkira.portal.certificate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Bram Baeyens
 */
@Name(Certificate.NAME)
@Scope(ScopeType.CONVERSATION)
public class Certificate implements Serializable {

	public static final String NAME = "be.fedict.eid.pkira.portal.certificate";

	private static final long serialVersionUID = 5429725347686453892L;

	private List<String> alternativeNames;
	private String distinguishedName;
	private Integer id;
	private String issuer;
	private String requesterName;
	private String serialNumber;
	private CertificateType type;
	private Date validityEnd;
	private Date validityStart;
	private String x509;
	private byte[] zippedCertificates;

	public List<String> getAlternativeNames() {
		return alternativeNames;
	}

	public String getAlternativeNamesAsString() {
		StringBuilder builder = new StringBuilder();
		for(String alternativeName: alternativeNames) {
			if (builder.length()!=0) {
				builder.append(" / ");
				builder.append(alternativeName);
			}
		}
		
		return builder.toString();
	}

	public String getDistinguishedName() {
		return distinguishedName;
	}

	public Integer getId() {
		return id;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public CertificateType getType() {
		return type;
	}

	public Date getValidityEnd() {
		return validityEnd;
	}

	public Date getValidityStart() {
		return validityStart;
	}

	public String getX509() {
		return x509;
	}

	public byte[] getZippedCertificates() {
		return zippedCertificates;
	}

	public void setAlternativeNames(List<String> alternativeNames) {
		this.alternativeNames = alternativeNames;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setType(CertificateType type) {
		this.type = type;
	}

	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}
	
	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}

	public void setX509(String x509) {
		this.x509 = x509;
	}

	
	public void setZippedCertificates(byte[] zip) {
		this.zippedCertificates = zip;
	}

	
	@Override
	public String toString() {
		return new StringBuilder("Certificate[").append("id=").append(id).append(", number=").append(serialNumber)
				.append(", type=").append(type).append(", validityStart=").append(validityStart)
				.append(", validityEnd=").append(validityEnd).append(", issuer=").append(issuer)
				.append(", distinguishedName=").append(distinguishedName).append(", requestName=")
				.append(requesterName).append(']').toString();
	}
}
