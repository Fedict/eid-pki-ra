/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
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

import be.fedict.eid.pkira.crypto.exception.CryptoException;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

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
     */
    @SuppressWarnings("deprecation")
    public String getSubject() {
        return certificationRequest.getCertificationRequestInfo().getSubject().toString();
    }

    public List<String> getSubjectAlternativeNames() throws CryptoException {
        List<String> result = new ArrayList<String>();

        ASN1Set attributes = certificationRequest.getCertificationRequestInfo().getAttributes();
        for (DERSet extension : getElementsFromASN1Set(attributes, CSR_EXTENSION_ATTRIBUTE_ID, DERSet.class)) {
            for (DEROctetString extensionValue : getElementsFromASN1Set(extension, X509Extension.subjectAlternativeName, DEROctetString.class)) {
                try {
                    ASN1Object bytes = ASN1Object.fromByteArray(extensionValue.getOctets());
                    GeneralNames names = GeneralNames.getInstance(bytes);
                    for (GeneralName name : names.getNames()) {
                        if (name.getTagNo() == GeneralName.dNSName) {
                            String theName = name.getName().toString();
                            if (theName.indexOf('*') != -1) {
                                throw new CryptoException("Subject Alternative Names are not allowed to contain wildcards.");
                            }
                            result.add(theName);
                        } else {
                            throw new CryptoException("Only Subject Alternative Name of type DNS is allowed in the CSR.");
                        }
                    }
                } catch (IOException e) {
                    throw new CryptoException("Could not extract SAN value.", e);
                }
            }
        }

        return result;
    }

    public static <T> List<T> getElementsFromASN1Set(ASN1Set set, ASN1ObjectIdentifier requiredObjectIdentifier, Class<T> expectedClass) {
        List<T> result = new ArrayList<T>();
        if (set != null) {
            for (int i = 0; i < set.size(); i++) {
                ASN1Sequence sequence = (ASN1Sequence) set.getObjectAt(i);
                getElementsFromASN1Sequence(sequence, requiredObjectIdentifier, expectedClass, result);
            }
        }

        return result;
    }

    public static <T> void getElementsFromASN1Sequence(ASN1Sequence sequence, ASN1ObjectIdentifier requiredObjectIdentifier, Class<T> expectedClass, List<T> resultList) {
        for (int j = 0; j < sequence.size(); j++) {
            DEREncodable object = sequence.getObjectAt(j);
            if (object instanceof DERSequence) {
                getElementsFromASN1Sequence((DERSequence) object, requiredObjectIdentifier, expectedClass, resultList);
            } else {
                ASN1ObjectIdentifier objectIdentifier = (ASN1ObjectIdentifier) object;
                if (objectIdentifier.equals(requiredObjectIdentifier)) {
                    resultList.add(expectedClass.cast(sequence.getObjectAt(j + 1)));
                }
                j++;
            }
        }
    }

    /**
     * Returns the DER encoded version of the CSR.
     */
    public byte[] getDerEncoded() {
        return certificationRequest.getDEREncoded();
    }

    /**
     * Returns the PEM encoded CSR.
     */
    public String getPemEncoded() {
        StringWriter writer = new StringWriter();
        PEMWriter pemWriter = new PEMWriter(writer);

        try {
            pemWriter.writeObject(certificationRequest);
            pemWriter.flush();
            pemWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    /**
     * Returns the length of the key.
     */
    public int getKeyLength() {
        return getKeyLength(getPublicKey());
    }

    private RSAPublicKey getPublicKey() {
        try {
            return (RSAPublicKey) certificationRequest.getPublicKey();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private int getKeyLength(RSAKey rsaKey) {
        int bitLength = rsaKey.getModulus().bitLength();

        int length = 128;
        while (bitLength > length) {
            length *= 2;
        }

        return length;
    }

    @Override
    public String toString() {
        return "CSRInfo[" + "subject=" + getSubject() + ']';
    }
}
