/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
package be.fedict.eid.pkira.crypto.csr;

import java.io.IOException;
import java.io.StringReader;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;

import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.crypto.util.BouncyCastleProviderUser;

/**
 * Class used to parse a CSR and extract the required fields.
 * 
 * @author Jan Van den Bergh
 */
public class CSRParserImpl extends BouncyCastleProviderUser implements CSRParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CSRInfo parseCSR(byte[] csr) throws CryptoException {
		return extractCSRInfo(new PKCS10CertificationRequest(csr));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CSRInfo parseCSR(String csr) throws CryptoException {
		PEMReader reader = new PEMReader(new StringReader(csr));
		Object pemObject;
		try {
			pemObject = reader.readObject();
			reader.close();
		} catch (IOException e) {
			throw new CryptoException("Could not read CSR from string: " + e.getMessage(), e);
		}

		if (pemObject instanceof PKCS10CertificationRequest) {
			PKCS10CertificationRequest certificationRequest = (PKCS10CertificationRequest) pemObject;
			return extractCSRInfo(certificationRequest);
		}

		throw new CryptoException("No CSR found.");
	}

	private CSRInfo extractCSRInfo(PKCS10CertificationRequest certificationRequest) throws CryptoException {
		try {
			if (!certificationRequest.verify()) {
				throw new CryptoException("CSR signature is not correct.");
			}
		} catch (Exception e) {
			throw new CryptoException("Cannot verify CSR signature: " + e.getMessage(), e);
		}

		CSRInfo csrInfo = new CSRInfo(certificationRequest);

		return csrInfo;
	}

}
