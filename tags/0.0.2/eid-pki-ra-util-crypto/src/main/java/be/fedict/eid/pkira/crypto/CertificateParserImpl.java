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
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.openssl.PEMReader;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Implementation of the certificate parser.
 * 
 * @author Jan Van den Bergh
 */
@Name(CertificateParser.NAME)
public class CertificateParserImpl extends BouncyCastleProviderUser implements CertificateParser {

	@Logger
	private Log log;
	
	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.pkira.crypto.CertificateParser#parseCertificate(java.lang
	 * .String)
	 */
	@Override
	public CertificateInfo parseCertificate(String certificateStr) throws CryptoException {
		log.debug(">>> parseCertificate(certificate[{0}])", certificateStr);

		PEMReader reader = new PEMReader(new StringReader(certificateStr));
		Object pemObject;
		try {
			pemObject = reader.readObject();
		} catch (IOException e) {
			log.info("<<< parseCertificate: Could not read certificate from string: ", e);
			throw new CryptoException("Could not read certificate from string: " + e.getMessage(), e);
		}
		
		if (pemObject instanceof X509Certificate) {
			X509Certificate certificate = (X509Certificate) pemObject;
			
			String issuer = certificate.getIssuerDN().getName();
			String subject = certificate.getSubjectDN().getName();
			Date notBefore = certificate.getNotBefore();
			Date notAfter = certificate.getNotAfter();
			BigInteger serialNumber = certificate.getSerialNumber();
			CertificateInfo certificateInfo = new CertificateInfo(issuer, subject, notBefore, notAfter, serialNumber);
			log.debug("<<< parseCertificate: {0}", certificateInfo);
			return certificateInfo;
		}

		log.info("<<< parseCertificate: No CSR found.");
		throw new CryptoException("No CSR found.");
	}

	/**
	 * Injects the logger.
	 */
	public void setLog(Log log) {
		this.log = log;
	}

}
