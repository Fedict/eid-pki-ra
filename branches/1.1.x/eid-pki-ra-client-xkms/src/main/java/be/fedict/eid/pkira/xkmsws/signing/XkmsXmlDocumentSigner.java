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

package be.fedict.eid.pkira.xkmsws.signing;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import org.w3c.dom.Document;

import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.crypto.xmlsign.XmlDocumentSigner;
import be.fedict.eid.pkira.xkmsws.XKMSClientException;
import be.fedict.eid.pkira.xkmsws.keyinfo.KeyStoreKeyProvider;

public class XkmsXmlDocumentSigner extends XmlDocumentSigner {

	public static final String PARAMETER_SIGNING_KEY_PROVIDER_CLASS = "signing.provider";
	public static final String ELEMENT_TO_APPEND_TO = "BulkRegister";
	public static final String ELEMENT_TO_SIGN = "SignedPart";

	private final Map<String, String> parameters;
	
	public XkmsXmlDocumentSigner(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void signXKMSDocument(Document document, String elementToAppendToName, String elementToSignName)
			throws XKMSClientException {
		// Instantiate the SigningKeyProvider
		KeyStoreKeyProvider signingKeyProvider = instantiateSigningKeyProvider();
		X509Certificate certificate = signingKeyProvider.getCertificate();
		PrivateKey privateKey = signingKeyProvider.getPrivateKey();

		// Sign the document
		try {
			signXMLDocument(document, certificate, privateKey, elementToAppendToName, elementToSignName);
		} catch (CryptoException e) {
			throw new XKMSClientException(e);
		}
	}

	private KeyStoreKeyProvider instantiateSigningKeyProvider() throws XKMSClientException {
		try {
			String providerClassName = parameters.get(PARAMETER_SIGNING_KEY_PROVIDER_CLASS);
			Class<?> providerClass = Class.forName(providerClassName);
			KeyStoreKeyProvider provider = (KeyStoreKeyProvider) providerClass.newInstance();
			provider.setParameters(parameters);

			return provider;
		} catch (Exception e) {
			throw new XKMSClientException("Error creating signing key provider.", e);
		}
	}

}
