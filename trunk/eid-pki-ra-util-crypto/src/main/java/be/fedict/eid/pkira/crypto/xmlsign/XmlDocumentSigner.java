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

package be.fedict.eid.pkira.crypto.xmlsign;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import be.fedict.eid.pkira.crypto.exception.CryptoException;

public class XmlDocumentSigner {

	public static final String PARAMETER_SIGNING_KEY_PROVIDER_CLASS = "signing.provider";
	public static final String ELEMENT_TO_APPEND_TO = "BulkRegister";
	public static final String ELEMENT_TO_SIGN = "SignedPart";

	private final XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

	public void signXMLDocument(Document document, X509Certificate certificate, PrivateKey privateKey,
			Element elementToAppendTo, Element elementToSign) throws CryptoException {
		try {
			String referenceUri = "#" + elementToSign.getAttribute("Id");
			DigestMethod digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA1, null);
			Transform transform = signatureFactory.newTransform(CanonicalizationMethod.ENVELOPED,
					(TransformParameterSpec) null);
			List<Transform> transforms = Collections.singletonList(transform);
			Reference reference = signatureFactory.newReference(referenceUri, digestMethod, transforms, null, null);

			// Create signed info
			CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(
					CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null);
			SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
			SignedInfo signedInfo = signatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod,
					Collections.singletonList(reference));

			// Create the KeyInfo containing the X509Data.
			KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
			List<Object> x509Content = new ArrayList<Object>();
			x509Content.add(certificate.getSubjectX500Principal().toString());
			x509Content.add(certificate);
			KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyInfoFactory
					.newX509Data(x509Content)));

			// Create a DOMSignContext and specify the RSA PrivateKey and
			// location of the resulting XMLSignature's parent element.
			DOMSignContext domSigningContext = new DOMSignContext(privateKey, elementToAppendTo);
            domSigningContext.setIdAttributeNS(elementToSign, null, "Id");
			domSigningContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");

			// Create the XMLSignature, but don't sign it yet.
			XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, keyInfo);

			// Marshal, generate, and sign the enveloped signature.
			xmlSignature.sign(domSigningContext);
		} catch (GeneralSecurityException e) {
			throw new CryptoException("Error signing XKMS document.", e);
		} catch (MarshalException e) {
			throw new CryptoException("Error signing XKMS document.", e);
		} catch (XMLSignatureException e) {
			throw new CryptoException("Error signing XKMS document.", e);
		}
	}

	public void signXMLDocument(Document document, X509Certificate certificate, PrivateKey privateKey,
			String elementToAppendToName, String elementToSignName) throws CryptoException {
		// Look up elements by name
		Element elementToAppendTo = getElementByTagName(document, elementToAppendToName);
		Element elementToSign = getElementByTagName(document, elementToSignName);

		// Sign it
		signXMLDocument(document, certificate, privateKey, elementToAppendTo, elementToSign);
	}

	private Element getElementByTagName(Document parentElement, String tagName) {
		Element signature = (Element) parentElement.getElementsByTagName(tagName).item(0);
		if (signature == null) {
			// JBossWS >< SunWS workaround
			signature = (Element) parentElement.getElementsByTagNameNS("*", tagName).item(0);
		}
		return signature;
	}

}
