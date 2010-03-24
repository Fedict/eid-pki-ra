/**
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

package be.fedict.eid.pkira.authentication;

import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.saml2.binding.decoding.HTTPPostDecoder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.Subject;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bram Baeyens
 */
public class Saml2RequestDecoder implements AuthenticationRequestDecoder {

	private static final Logger LOG = LoggerFactory.getLogger(Saml2RequestDecoder.class);

	public Saml2RequestDecoder() {
		bootstrapSaml2();
	}

	public EIdUser decode(HttpServletRequest saml2Request) throws AuthenticationException {
		LOG.debug(">>> decode(saml2Request[{0}])", saml2Request);

		SAMLObject samlObject = getDecodedSamlObject(saml2Request);
		Response samlResponse;
		try {
			samlResponse = (Response) samlObject;
		} catch (ClassCastException e) {
			LOG.debug("<<< decode: Expected a SAML2 Response document");
			throw new AuthenticationException("Expected a SAML2 Response document");
		}

		if (LOG.isDebugEnabled() || true /* TODO Remove true */) {
			try {
				StringWriter writer = new StringWriter();
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(new DOMSource(samlResponse.getDOM()), new StreamResult(writer));
				LOG.debug("SAML Response: " + writer.toString());
			} catch (Exception e) {
				// ignore this
			}
		}

		validateSamlResponse(samlResponse);

		List<Assertion> assertions = samlResponse.getAssertions();
		if (assertions.isEmpty()) {
			LOG.debug("<<< decode: Missing SAML assertions");
			throw new AuthenticationException("Missing SAML assertions");
		}

		Assertion assertion = assertions.get(0);
		List<AuthnStatement> authnStatements = assertion.getAuthnStatements();
		if (authnStatements.isEmpty()) {
			LOG.debug("<<< decode: Missing SAML authn statement");
			throw new AuthenticationException("Missing SAML authn statement");
		}

		Subject subject = assertion.getSubject();
		NameID nameId = subject.getNameID();

		EIdUser samlUser = new EIdUser();
		samlUser.setRRN(nameId.getValue());
		// TODO Bram get this from Saml object
		samlUser.setFirstName("John");
		samlUser.setLastName("Doe");
		LOG.debug(">>> decode: {0}", samlUser);
		return samlUser;
	}

	private void bootstrapSaml2() {
		LOG.debug(">>> bootstrap()");
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			LOG.debug("<<< bootstrap: OpenSAML configuration exception");
			throw new RuntimeException("OpenSAML configuration exception, impossible to bootstrap.");
		}
		LOG.debug("<<< bootstrap");
	}

	private SAMLObject getDecodedSamlObject(HttpServletRequest saml2Request) throws AuthenticationException {
		LOG.debug(">>> getDecodedSamlObject(saml2Request[{0}])", saml2Request);
		BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject> messageContext = new BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject>();
		messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(saml2Request));

		SAMLMessageDecoder decoder = new HTTPPostDecoder();
		try {
			decoder.decode(messageContext);
		} catch (MessageDecodingException e) {
			throw new AuthenticationException("OpenSAML message decoding error");
		} catch (SecurityException e) {
			LOG.debug("<<< getDecodedSamlObject: OpenSAML security error");
			throw new AuthenticationException("OpenSAML security error", e);
		} 
		
		SAMLObject samlObject = messageContext.getInboundSAMLMessage();
		LOG.debug("<<< getDecodedSamlObject: {0}", samlObject);
		return samlObject;
	}

	private void validateSamlResponse(Response samlResponse) throws AuthenticationException {
		Status status = samlResponse.getStatus();
		StatusCode statusCode = status.getStatusCode();
		String statusValue = statusCode.getValue();
		if (!StatusCode.SUCCESS_URI.equals(statusValue)) {
			LOG.debug("<<< decode: No successful SAML response");
			throw new AuthenticationException("No successful SAML response");
		}
	}
}
