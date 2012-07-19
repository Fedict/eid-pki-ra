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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;

import be.fedict.eid.pkira.crypto.exception.CryptoException;

/**
 * Information extracted from a CSR.
 * 
 * @author Jan Van den Bergh
 */
public class CSRInfo {

	private static final ASN1ObjectIdentifier CSR_EXTENSION_ATTRIBUTE_ID = new ASN1ObjectIdentifier(
			"1.2.840.113549.1.9.14");

	private final PKCS10CertificationRequest certificationRequest;

	public CSRInfo(PKCS10CertificationRequest certificationRequest) {
		this.certificationRequest = certificationRequest;
	}

	/**
	 * Returns the subject (distinguished name) found in the CSR.
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getSubject() {
		return certificationRequest.getCertificationRequestInfo().getSubject().toString();
	}

	public List<String> getSubjectAlternativeNames() throws CryptoException {
		List<String> result = new ArrayList<String>();

		ASN1Set attributes = certificationRequest.getCertificationRequestInfo().getAttributes();
		for (DERSet extension : getElementsFromASN1Set(attributes, CSR_EXTENSION_ATTRIBUTE_ID, DERSet.class)) {
			for (DEROctetString extensionValue : getElementsFromASN1Set(extension,
					X509Extension.subjectAlternativeName, DEROctetString.class)) {
				try {
					ASN1Object bytes = ASN1Object.fromByteArray(extensionValue.getOctets());
					GeneralNames names = GeneralNames.getInstance(bytes);
					for (GeneralName name : names.getNames()) {
						if (name.getTagNo() == GeneralName.dNSName) {
							String theName = name.getName().toString();
							if (theName.indexOf('*') != -1) {
								throw new CryptoException(
										"Subject Alternative Names are not allowed to contain wildcards.");
							}
							result.add(theName);
						} else {
							throw new CryptoException(
									"Only Subject Alternative Name of type SAN is allowed in the CSR.");
						}
					}
				} catch (IOException e) {
					throw new CryptoException("Could not extract SAN value.", e);
				}
			}
		}

		return result;
	}

	public static <T> List<T> getElementsFromASN1Set(ASN1Set set, ASN1ObjectIdentifier requiredObjectIdentifier,
			Class<T> expectedClass) {
		List<T> result = new ArrayList<T>();
		if (set != null) {
			for (int i = 0; i < set.size(); i++) {
				DERSequence sequence = (DERSequence) set.getObjectAt(i);
				DEREncodable object = sequence.getObjectAt(0);
				while (object instanceof DERSequence) {
					sequence = (DERSequence) object;
					object = sequence.getObjectAt(0);
				}

				ASN1ObjectIdentifier identifier = (ASN1ObjectIdentifier) object;
				if (identifier.equals(requiredObjectIdentifier)) {
					result.add(expectedClass.cast(sequence.getObjectAt(1)));
				}
			}
		}

		return result;
	}

	/**
	 * Returns the DER encoded version of the CSR.
	 * 
	 * @return
	 */
	public byte[] getDerEncoded() {
		return certificationRequest.getDEREncoded();
	}

	/**
	 * Returns the PEM encoded CSR.
	 * 
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
		return new StringBuilder("CSRInfo[").append("subject=").append(getSubject()).append(']').toString();
	}
}
