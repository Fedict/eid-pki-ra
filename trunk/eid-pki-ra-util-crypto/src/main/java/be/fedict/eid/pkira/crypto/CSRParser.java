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
import java.io.StringReader;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;


/**
 * Class used to parse a CSR and extract the required fields.
 * @author Jan Van den Bergh
 */
public class CSRParser {
	
	public static CSRInfo parseCSR(String csr) throws CryptoException {
		PEMReader reader = new PEMReader(new StringReader(csr));
		Object pemObject;
		try {
			pemObject = reader.readObject();
		} catch (IOException e) {
			throw new CryptoException("Could not read CSR from string: " + e.getMessage(), e);
		}		
		
		if (pemObject instanceof CertificationRequest) {
			PKCS10CertificationRequest certificationRequest = (PKCS10CertificationRequest) pemObject;
			try {
				if (!certificationRequest.verify()) {
					throw new CryptoException("CSR signature is not correct.");
				}
			} catch (Exception e) {
				throw new CryptoException("Cannot verify CSR signature: " + e.getMessage(), e);
			}
			
			String dn = certificationRequest.getCertificationRequestInfo().getSubject().toString();			
			return new CSRInfo(dn);
		}				
		
		throw new CryptoException("No CSR found.");
	}	
}
