/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
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

package be.fedict.eid.pkira.xkmsws;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import be.fedict.eid.pkira.xkmsws.keyinfo.KeyStoreKeyProvider;

public class XKMSSignatureSOAPHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String PARAMETER_SIGNING_KEY_PROVIDER_CLASS = "signing.provider";

	private static final Log LOG = LogFactory.getLog(XKMSSignatureSOAPHandler.class);

	private final XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

	private Map<String, String> parameters;

	public XKMSSignatureSOAPHandler(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public void close(MessageContext context) {
		LOG.debug("close");
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		LOG.debug("adding XML signature");

		Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			try {
				Element soapRootElement = context.getMessage().getSOAPBody();

				Element bulkRegisterElement = (Element) soapRootElement.getElementsByTagName("BulkRegister").item(0);
				if (bulkRegisterElement == null) {
					// JBossWS >< SunWS workaround
					bulkRegisterElement = (Element) soapRootElement.getElementsByTagNameNS("*", "BulkRegister").item(0);
				}
				
				Element signedPartElement = (Element) bulkRegisterElement.getElementsByTagName("SignedPart").item(0);
				if (signedPartElement == null) {
					// JBossWS >< SunWS workaround
					signedPartElement = (Element) soapRootElement.getElementsByTagNameNS("*", "SignedPart").item(0);
				}

				// Create reference with digest method and transforms
				String referenceUri = "#" + signedPartElement.getAttribute("Id");
				DigestMethod digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA1, null);
				Transform transform = signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
				List<Transform> transforms = Collections.singletonList(transform);
				Reference reference = signatureFactory.newReference(referenceUri, digestMethod, transforms, null, null);

				// Create signed info
				SignedInfo signedInfo = signatureFactory.newSignedInfo(signatureFactory.newCanonicalizationMethod(
						CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), signatureFactory
						.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(reference));

				// Instantiate the SigningKeyProvider
				KeyStoreKeyProvider signingKeyProvider = instantiateSigningKeyProvider();
				X509Certificate certificate = signingKeyProvider.getCertificate();
				PrivateKey privateKey = signingKeyProvider.getPrivateKey();

				// Create the KeyInfo containing the X509Data.
				KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
				List<Object> x509Content = new ArrayList<Object>();
				x509Content.add(certificate.getSubjectX500Principal().getName());
				x509Content.add(certificate);
				KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyInfoFactory
						.newX509Data(x509Content)));

				// Create a DOMSignContext and specify the RSA PrivateKey and
				// location of the resulting XMLSignature's parent element.
				DOMSignContext domSigningContext = new DOMSignContext(privateKey, bulkRegisterElement);

				// Create the XMLSignature, but don't sign it yet.
				XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);

				// Marshal, generate, and sign the enveloped signature.
				signature.sign(domSigningContext);
			} catch (Exception e) {
				throw new RuntimeException("Error creating xml signature", e);
			}
		}

		return true;
	}

	private KeyStoreKeyProvider instantiateSigningKeyProvider() throws XMLSigningException {
		try {
			String providerClassName = parameters.get(PARAMETER_SIGNING_KEY_PROVIDER_CLASS);
			Class<?> providerClass = Class.forName(providerClassName);
			KeyStoreKeyProvider provider = (KeyStoreKeyProvider) providerClass.newInstance();
			provider.setParameters(parameters);

			return provider;
		} catch (Exception e) {
			throw new XMLSigningException("Error creating signing key provider.", e);
		}
	}
}
