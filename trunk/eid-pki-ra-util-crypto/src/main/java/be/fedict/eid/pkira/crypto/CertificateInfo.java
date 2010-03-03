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
package be.fedict.eid.pkira.crypto;

import java.util.Date;

/**
 * Information extracted from a certificate.
 * 
 * @author Jan Van den Bergh
 */
public class CertificateInfo {

	private final String subject, issuer;
	private final Date validityStart, validityEnd;

	public CertificateInfo(String issuer, String subject, Date validityStart, Date validityEnd) {
		super();
		this.issuer = issuer;
		this.subject = subject;
		this.validityStart = validityStart;
		this.validityEnd = validityEnd;
	}

	public String getSubject() {
		return subject;
	}

	public String getIssuer() {
		return issuer;
	}

	public Date getValidityStart() {
		return validityStart;
	}

	public Date getValidityEnd() {
		return validityEnd;
	}
}
