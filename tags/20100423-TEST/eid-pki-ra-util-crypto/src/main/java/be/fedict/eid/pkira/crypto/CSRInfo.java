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

import java.io.IOException;
import java.io.StringWriter;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;

/**
 * Information extracted from a CSR.
 * 
 * @author Jan Van den Bergh
 */
public class CSRInfo {

	private PKCS10CertificationRequest certificationRequest;

	public CSRInfo(PKCS10CertificationRequest certificationRequest) {
		this.certificationRequest = certificationRequest;
	}

	/**
	 * Returns the subject (distinguished name) found in the CSR.
	 * @return
	 */
	public String getSubject() {
		return certificationRequest.getCertificationRequestInfo().getSubject().toString();
	}
	
	/**
	 * Returns the DER encoded version of the CSR.
	 * @return 
	 */
	public byte[] getDerEncoded() {
		return certificationRequest.getDEREncoded();
	}
	
	/**
	 * Returns the PEM encoded CSR.
	 * @return
	 */
	public String getPemEncoded() {
		StringWriter writer = new StringWriter();
		PEMWriter pemWriter = new PEMWriter(writer);
		
		try {
			pemWriter.writeObject(certificationRequest);
			pemWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return writer.toString();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("CSRInfo[")
			.append("subject=").append(getSubject())
			.append(']').toString();
	}
}
