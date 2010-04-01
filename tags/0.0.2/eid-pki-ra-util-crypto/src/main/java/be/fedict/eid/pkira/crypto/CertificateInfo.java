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

import java.math.BigInteger;
import java.util.Date;

/**
 * Information extracted from a certificate.
 * 
 * @author Jan Van den Bergh
 */
public class CertificateInfo {

	private final String distinguishedName, issuer;
	private final Date validityStart, validityEnd;
	private final BigInteger serialNumber;

	public CertificateInfo(String issuer, String distinguishedName, Date validityStart, Date validityEnd, BigInteger serialNumber) {
		this.issuer = issuer;
		this.distinguishedName = distinguishedName;
		this.validityStart = validityStart;
		this.validityEnd = validityEnd;
		this.serialNumber = serialNumber;
	}
	
	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public String getDistinguishedName() {
		return distinguishedName;
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
