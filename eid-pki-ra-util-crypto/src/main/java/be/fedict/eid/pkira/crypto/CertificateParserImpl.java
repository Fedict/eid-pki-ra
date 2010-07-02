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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CertificateInfo parseCertificate(byte[] certificateData) throws CryptoException {
		log.debug(">>> parseCertificate(certificateData[{0}])", certificateData);
		
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());
			X509Certificate certificate = (X509Certificate) certificateFactory
					.generateCertificate(new ByteArrayInputStream(certificateData));
			if (certificate==null) {
				throw new CryptoException("Cannot parse certificate");
			}
			
			return extractCertificateInfo(certificate);
		} catch (CertificateException e) {
			throw new CryptoException("Cannot parse certificate", e);
		}
	}

	/**
	 * {@inheritDoc}
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
			return extractCertificateInfo((X509Certificate) pemObject);
		}

		log.info("<<< parseCertificate: No CSR found.");
		throw new CryptoException("No CSR found.");
	}

	private CertificateInfo extractCertificateInfo(X509Certificate certificate) {
		CertificateInfo certificateInfo = new CertificateInfo(certificate);
		log.debug("<<< parseCertificate: {0}", certificateInfo);
		
		return certificateInfo;
	}

	/**
	 * Injects the logger.
	 */
	public void setLog(Log log) {
		this.log = log;
	}

}
