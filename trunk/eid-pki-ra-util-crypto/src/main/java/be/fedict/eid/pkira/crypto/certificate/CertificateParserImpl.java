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
package be.fedict.eid.pkira.crypto.certificate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.crypto.util.BouncyCastleProviderUser;

/**
 * Implementation of the certificate parser.
 * 
 * @author Jan Van den Bergh
 */
public class CertificateParserImpl extends BouncyCastleProviderUser implements CertificateParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CertificateInfo parseCertificate(byte[] certificateData) throws CryptoException {
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
		PEMReader reader = new PEMReader(new StringReader(certificateStr));
		Object pemObject;
		try {
			pemObject = reader.readObject();
		} catch (IOException e) {
			throw new CryptoException("Could not read certificate from string: " + e.getMessage(), e);
		}

		if (pemObject instanceof X509Certificate) {
			return extractCertificateInfo((X509Certificate) pemObject);
		}

		throw new CryptoException("No CSR found.");
	}

	private CertificateInfo extractCertificateInfo(X509Certificate certificate) {
		CertificateInfo certificateInfo = new CertificateInfo(certificate);
		
		return certificateInfo;
	}
}
