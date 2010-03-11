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

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.annotations.Name;

/**
 * @author Bram Baeyens
 *
 */
@Name("certificate")
public class Certificate implements Serializable {

	private static final long serialVersionUID = 5429725347686453892L;
	
	private Long number;
	private Date expirationDate;
	private String content;
	private CertificateType type;
	
	private CSRInfo distinguishedName;
	@Enumerated(EnumType.STRING)
	private CertificateType certificateType;
	@In(create=true)
	private Operator operator;
	private String description;
	private String legalNotice = "testLegalNotice";
	private String base64Csr;
	
	public Long getNumber() {
		return number;
	}
	
	public void setNumber(Long number) {
		this.number = number;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public void setType(CertificateType type) {
		this.type = type;
	}

	public CertificateType getType() {
		return type;
	}

	@Override
	public String toString() {
		return new StringBuilder("Certificate[")
				.append("number").append(number)
				.append(", type").append(type)
				.append(", expirationDate").append(expirationDate)
				.append(", content").append(content)
				.append(']').toString();
	}
}
