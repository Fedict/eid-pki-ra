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

package be.fedict.eid.pkira.portal.certificate;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


/**
 * @author Bram Baeyens
 *
 */
@Name(Certificate.NAME)
@Scope(ScopeType.CONVERSATION)
public class Certificate implements Serializable {

	private static final long serialVersionUID = 5429725347686453892L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.certificate";
	
	private Integer id;
	private String serialNumber;
	private CertificateType type;
	private Date validityStart;
	private Date validityEnd;
	private String x509;
	private String issuer;
	private String requesterName;
	private String distinguishedName;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public CertificateType getType() {
		return type;
	}

	public void setType(CertificateType type) {
		this.type = type;
	}

	public Date getValidityStart() {
		return validityStart;
	}

	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}

	public Date getValidityEnd() {
		return validityEnd;
	}

	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}

	public String getX509() {
		return x509;
	}

	public void setX509(String x509) {
		this.x509 = x509;
	}

	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getRequesterName() {
		return requesterName;
	}
	
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	@Override
	public String toString() {
		return new StringBuilder("Certificate[")
				.append("id=").append(id)
				.append(", number=").append(serialNumber)
				.append(", type=").append(type)
				.append(", validityStart=").append(validityStart)
				.append(", validityEnd=").append(validityEnd)
				.append(", issuer=").append(issuer)
				.append(", distinguishedName=").append(distinguishedName)
				.append(", requestName=").append(requesterName)
				.append(']').toString();
	}
}
