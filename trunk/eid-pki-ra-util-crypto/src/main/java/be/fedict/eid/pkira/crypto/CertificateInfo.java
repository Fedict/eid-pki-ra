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
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.openssl.PEMWriter;

/**
 * Information extracted from a certificate.
 * 
 * @author Jan Van den Bergh
 */
public class CertificateInfo {

	private X509Certificate certificate;

	public CertificateInfo(X509Certificate certificate) {
		this.certificate = certificate;
	}
	
	public BigInteger getSerialNumber() {
		return certificate.getSerialNumber();
	}

	public String getDistinguishedName() {
		return certificate.getSubjectDN().getName();
	}

	public String getIssuer() {
		return certificate.getIssuerDN().getName();
	}

	public Date getValidityStart() {
		return certificate.getNotBefore();
	}

	public Date getValidityEnd() {
		return certificate.getNotAfter();
	}
	
	/**
	 * Returns the DER encoded version of the CSR.
	 * @return 
	 */
	public byte[] getDerEncoded() {
		try {
			return certificate.getEncoded();
		} catch (CertificateEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns the PEM encoded CSR.
	 * @return
	 */
	public String getPemEncoded() {
		StringWriter writer = new StringWriter();
		PEMWriter pemWriter = new PEMWriter(writer);
		
		try {
			pemWriter.writeObject(certificate);
			pemWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return writer.toString();
	}
	
	public boolean isSelfSigned() throws CryptoException {
		return isSignedBy(certificate.getPublicKey());
		
	}

	public boolean isSignedBy(PublicKey publicKey) throws CryptoException {
		try {
			certificate.verify(publicKey);
			return true;
		} catch (SignatureException e) {
			return false;
		} catch (InvalidKeyException e) {
			return false;
		} catch (Exception e) {
			throw new CryptoException("Error validating signature.", e);
		}
	}

	@Override
	public String toString() {
		return new StringBuilder("CertificateInfo[")
		.append("subject=").append(getDistinguishedName())
		.append(']').toString();
	}

	public X509Certificate getCertificate() {
		return certificate;
	}
}
