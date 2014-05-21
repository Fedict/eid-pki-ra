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
package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import be.fedict.eid.dss.client.DigitalSignatureServiceClient;
import be.fedict.eid.dss.client.NotParseableXMLDocumentException;
import be.fedict.eid.dss.client.SignatureInfo;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.blm.model.framework.WebserviceLocator;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserHome;
import be.fedict.eid.pkira.crypto.certificate.CertificateParser;
import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.RequestType;
import be.fedict.eid.pkira.generated.contracts.ResultType;
import be.fedict.eid.pkira.generated.contracts.SignatureType;
import be.fedict.eid.pkira.generated.contracts.X509DataType;

/**
 * Bean to verify the signature on a contract.
 * 
 * @author Jan Van den Bergh
 */
@Name(SignatureVerifier.NAME)
@Scope(ScopeType.STATELESS)
public class SignatureVerifierBean implements SignatureVerifier {

	private static final String ENCODING = "UTF-8";

	public static final String MIME_TYPE = "text/xml";

	@Logger
	private Log log;

	@In(value = WebserviceLocator.NAME, create = true)
	private WebserviceLocator webserviceLocator;

	@In(value = CertificateParser.NAME, create = true)
	private CertificateParser certificateParser;

	@In(value = UserHome.NAME, create = true)
	private UserHome userHome;

	@In(create = true, value = DistinguishedNameManager.NAME)
	private DistinguishedNameManager distinguishedNameManager;

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.eiddss.SignatureVerification#verifySignature(
	 * java.lang.String)
	 */
	@Override
	public String verifySignature(String requestMessage, RequestType request) throws ContractHandlerBeanException {
		// First see if we can validate the signature using a certificate in our
		// database
		String result = validateAgainstStoredCertificate(request, requestMessage);
		if (result != null) {
			return result;
		}

		// Then try the DSS webservice (for eID)
		return validateWithDSS(requestMessage);
	}

	private String validateAgainstStoredCertificate(RequestType request, String requestMessage)
			throws ContractHandlerBeanException {
		// TODO Unit test this branch
		// Get the DN out of the signature
		String signingDN = null;
		if (request instanceof CertificateSigningRequestType) {
			CertificateSigningRequestType signingRequest = (CertificateSigningRequestType) request;
			if (signingRequest.getSignature() != null) {
				signingDN = extractSigningDN(signingRequest.getSignature());
			}
		}
		if (request instanceof CertificateRevocationRequestType) {
			CertificateRevocationRequestType revocationRequest = (CertificateRevocationRequestType) request;
			if (revocationRequest.getSignature() != null) {
				signingDN = extractSigningDN(revocationRequest.getSignature());
			}
		}
		if (signingDN == null) {
			return null;
		}

		// Look up the user
		User user = userHome.findByCertificateSubject(signingDN);
		if (user == null) {
			return null;
		}

		// Validate the signature
		if (!validateSignature(requestMessage, user.getCertificate())) {
			throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, "The signature could not be verified.");
		}

		return signingDN;
	}

	private boolean validateSignature(String requestMessage, String certificate) {
		// Get the certificate key
		PublicKey certificateKey;
		try {
			certificateKey = certificateParser.parseCertificate(certificate).getCertificate().getPublicKey();
		} catch (CryptoException e) {
			throw new RuntimeException("Certificate should be valid.", e);
		}

		// Validate the signature
		try {
			// Read the document
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(requestMessage)));

			// Get the signature element
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
			if (nl.getLength() == 0) {
				return false; // no signature element
			}

			// Create validation context
			DOMValidateContext valContext = new DOMValidateContext(new SingletonKeySelector(certificateKey), nl.item(0));
			// Create signature factory and signature
			XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");
			XMLSignature signature = factory.unmarshalXMLSignature(valContext);

			// Check if valid
			return signature.validate(valContext);
		} catch (MarshalException e) {
			log.error("Invalid signature on contract.", e);
			return false;
		} catch (XMLSignatureException e) {
			log.error("Error validating signature on contract.", e);
			return false;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			log.error("Cannot parse contract contract.", e);
			return false;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String extractSigningDN(SignatureType signature) throws ContractHandlerBeanException {
		X509DataType x509DataType = extractFromJAXBElementList(signature.getKeyInfo().getContent(), X509DataType.class);
		if (x509DataType == null) {
			return null;
		}

		try {
			String certificateSubject;

			byte[] certificate = extractFromJAXBElementList(
					x509DataType.getX509IssuerSerialOrX509SKIOrX509SubjectName(), byte[].class);
			if (certificate != null) {
				certificateSubject = certificateParser.parseCertificate(certificate).getDistinguishedName();
			} else {
				certificateSubject = extractFromJAXBElementList(
						x509DataType.getX509IssuerSerialOrX509SKIOrX509SubjectName(), String.class);
			}

			if (certificateSubject == null) {
				return null;
			}

			return distinguishedNameManager.createDistinguishedName(certificateSubject).toString();
		} catch (InvalidDistinguishedNameException e) {
			log.error("Error parsing DN in contract.", e);
			return null;
		} catch (CryptoException e) {
			log.error("Error parsing certificate in contract.", e);
			return null;
		}

		// byte[] certificateBytes = extractFromJAXBElementList(
		// x509DataType.getX509IssuerSerialOrX509SKIOrX509SubjectName(),
		// byte[].class);
		// if (certificateBytes == null) {
		// return null;
		// }
		//
		// try {
		// return
		// certificateParser.parseCertificate(certificateBytes).getDistinguishedName();
		// } catch (CryptoException e) {
		// throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE,
		// "Invalid certificate found in the contract.");
		// }
	}

	private <T> T extractFromJAXBElementList(List<Object> list, Class<T> type) {
		for (Object item : list) {
			JAXBElement<?> element = (JAXBElement<?>) item;
			if (type.isAssignableFrom(element.getDeclaredType())) {
				return type.cast(element.getValue());
			}
		}

		return null;
	}

	private String validateWithDSS(String requestMessage) throws ContractHandlerBeanException {
		DigitalSignatureServiceClient dssClient = webserviceLocator.getDigitalSignatureServiceClient();
		try {
			// Call DSS to validate the signature
			List<SignatureInfo> signatureInfos = dssClient.verifyWithSigners(requestMessage.getBytes(ENCODING),
					MIME_TYPE);
			if (signatureInfos == null || signatureInfos.size() == 0) {
				throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, "Signature is invalid.");
			}

			if (signatureInfos.size() > 1) {
				throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE,
						"Only one signature is supported in the contract.");
			}

			// Extract the subject from the signature info
			SignatureInfo signatureInfo = signatureInfos.get(0);
			String subject = signatureInfo.getSigner().getSubjectDN().getName();

			// If this subject contains a National Register Number, get it out
			Pattern pattern = Pattern.compile("SERIALNUMBER=(\\d{11,11})");
			Matcher matcher = pattern.matcher(subject);
			if (matcher.find()) {
				subject = matcher.group(1);
			}

			return subject;
		} catch (RuntimeException e) {
			// eid-dss client throws runtime exception when something is wrong,
			// so let's handle this.
			log.error("Error during call to eid-dss to validate signature.", e);
			throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, "Error verifying signature", e);
		} catch (NotParseableXMLDocumentException e) {
			log.error("Error during call to eid-dss to validate signature.", e);
			throw new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, "Error verifying signature", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	protected void setLog(Log log) {
		this.log = log;
	}

	protected void setWebserviceLocator(WebserviceLocator webserviceLocator) {
		this.webserviceLocator = webserviceLocator;
	}
}
