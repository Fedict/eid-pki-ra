package be.fedict.eid.pkira.xkmsws.signing;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import be.fedict.eid.pkira.xkmsws.XKMSClientException;
import be.fedict.eid.pkira.xkmsws.keyinfo.KeyStoreKeyProvider;

public class XmlDocumentSigner {

	public static final String PARAMETER_SIGNING_KEY_PROVIDER_CLASS = "signing.provider";
	public static final String ELEMENT_TO_APPEND_TO = "BulkRegister";
	public static final String ELEMENT_TO_SIGN = "SignedPart";

	private final Map<String, String> parameters;
	private final XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

	public XmlDocumentSigner(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void signXKMSDocument(Document document, String elementToAppendToName, String elementToSignName)
			throws XKMSClientException {
		// Instantiate the SigningKeyProvider
		KeyStoreKeyProvider signingKeyProvider = instantiateSigningKeyProvider();
		X509Certificate certificate = signingKeyProvider.getCertificate();
		PrivateKey privateKey = signingKeyProvider.getPrivateKey();

		// Sign the docuemnt
		signXKMSDocument(document, certificate, privateKey, elementToAppendToName, elementToSignName);
	}

	public void signXKMSDocument(Document document, X509Certificate certificate, PrivateKey privateKey,
			String elementToAppendToName, String elementToSignName) throws XKMSClientException {
		try {
			// Bulk register element to work with
			Element elementToAppendTo = getElementByTagName(document, elementToAppendToName);

			// Create reference with digest method and transforms
			Element elementToSign = getElementByTagName(document, elementToSignName);
			String referenceUri = "#" + elementToSign.getAttribute("Id");
			DigestMethod digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA1, null);
			Transform transform = signatureFactory.newTransform(CanonicalizationMethod.INCLUSIVE,
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
			// x509Content.add(certificate);
			KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyInfoFactory
					.newX509Data(x509Content)));

			// Create a DOMSignContext and specify the RSA PrivateKey and
			// location of the resulting XMLSignature's parent element.
			DOMSignContext domSigningContext = new DOMSignContext(privateKey, elementToAppendTo);
			domSigningContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");

			// Create the XMLSignature, but don't sign it yet.
			XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, keyInfo);

			// Marshal, generate, and sign the enveloped signature.
			xmlSignature.sign(domSigningContext);
		} catch (GeneralSecurityException e) {
			throw new XKMSClientException("Error signing XKMS document.", e);
		} catch (MarshalException e) {
			throw new XKMSClientException("Error signing XKMS document.", e);
		} catch (XMLSignatureException e) {
			throw new XKMSClientException("Error signing XKMS document.", e);
		}

	}

	private Element getElementByTagName(Document parentElement, String tagName) {
		Element signature = (Element) parentElement.getElementsByTagName(tagName).item(0);
		if (signature == null) {
			// JBossWS >< SunWS workaround
			signature = (Element) parentElement.getElementsByTagNameNS("*", tagName).item(0);
		}
		return signature;
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
