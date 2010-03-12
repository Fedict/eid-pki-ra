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

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * Class used to parse a CSR and extract the required fields.
 * 
 * @author Jan Van den Bergh
 */
@Name(CSRParser.NAME)
@Scope(ScopeType.APPLICATION)
public class CSRParserImpl extends BouncyCastleProviderUser implements CSRParser {

	@Logger
	private Log log;

	/*
	 * (non-Javadoc)
	 * @see be.fedict.eid.pkira.crypto.CSRParser#parseCSR(java.lang.String)
	 */
	public CSRInfo parseCSR(String csr) throws CryptoException {
		log.debug(">>> parseCSR(csr[{0}])", csr);

		PEMReader reader = new PEMReader(new StringReader(csr));
		Object pemObject;
		try {
			pemObject = reader.readObject();
		} catch (IOException e) {
			log.info("<<< parseCSR: Could not read CSR from string: ", e);
			throw new CryptoException("Could not read CSR from string: " + e.getMessage(), e);
		}

		if (pemObject instanceof PKCS10CertificationRequest) {
			PKCS10CertificationRequest certificationRequest = (PKCS10CertificationRequest) pemObject;
			try {
				if (!certificationRequest.verify()) {
					log.info("<<< parseCSR: CSR signature is not correct.");
					throw new CryptoException("CSR signature is not correct.");
				}
			} catch (Exception e) {
				log.info("<<< parseCSR: Cannot verify CSR signature: ", e);
				throw new CryptoException("Cannot verify CSR signature: " + e.getMessage(), e);
			}

			String dn = certificationRequest.getCertificationRequestInfo().getSubject().toString();
			CSRInfo csrInfo = new CSRInfo(dn);

			log.debug("<<< parseCSR: {0}", csrInfo);
			return csrInfo;
		}

		log.info("<<< parseCSR: No CSR found.");
		throw new CryptoException("No CSR found.");
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
