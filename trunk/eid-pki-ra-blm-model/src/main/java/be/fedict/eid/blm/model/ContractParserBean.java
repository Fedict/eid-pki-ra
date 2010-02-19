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
package be.fedict.eid.blm.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.codec.binary.Base64;
import org.xml.sax.SAXException;

import be.fedict.eid.pkira.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.contracts.RequestType;
import be.fedict.eid.pkira.contracts.ResponseType;
import be.fedict.eid.pkira.contracts.ResultType;

/**
 * This class contains the code to marshal and unmarshal contract documents.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
public class ContractParserBean implements ContractParser {

	private static Logger LOGGER = Logger.getLogger(ContractParserBean.class.getName());
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	/**
	 * Initializes the bean.
	 */
	@PostConstruct
	public void setupBean() {
		try {
			// Create JAXBContext
			JAXBContext context = JAXBContext.newInstance(CertificateSigningRequestType.class.getPackage().getName());

			// Create schema
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL url = ContractHandlerBean.class.getClassLoader().getResource("xsd/eid-pki-ra.xsd");
			Schema schema = schemaFactory.newSchema(url);

			// Create marshaller and unmarshaller for this context and schema
			marshaller = context.createMarshaller();
			marshaller.setSchema(schema);

			unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(schema);
		} catch (JAXBException e) {
			LOGGER.log(Level.SEVERE, "Cannot create JAXBContext", e);
			throw new RuntimeException(e);
		} catch (SAXException e) {
			LOGGER.log(Level.SEVERE, "Cannot create schema", e);
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractParser#marshalResponseMessage(javax.xml
	 * .bind.JAXBElement)
	 */
	public String marshalResponseMessage(JAXBElement<? extends ResponseType> response) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			marshaller.marshal(response, outputStream);

			return new String(Base64.encodeBase64(outputStream.toByteArray()));
		} catch (JAXBException e) {
			LOGGER.log(Level.SEVERE, "Error marshalling response.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.ContractParser#unmarshalRequestMessage(java.lang
	 * .String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends RequestType> T unmarshalRequestMessage(String requestMsg, Class<T> requestClass)
			throws ContractHandlerBeanException {
		// Convert to bytes
		if (requestMsg == null) {
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, "Request is null.");
		}
		ByteArrayInputStream inputStream;
		try {
			byte[] msgBytes = Base64.decodeBase64(requestMsg);
			inputStream = new ByteArrayInputStream(msgBytes);
		} catch (Exception e) {
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, "Invalid base64 encoded data.");
		}

		// Unmarshal the message
		try {
			JAXBElement<? extends Object> result = (JAXBElement<? extends Object>) unmarshaller.unmarshal(inputStream);

			if (result == null) {
				throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, "Message contains null.");
			}
			if (result.getDeclaredType() != requestClass) {
				throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, "Message is of an invalid type: "
						+ result.getClass().getSimpleName());
			}

			return (T) result.getValue();
		} catch (JAXBException e) {
			String message = "Cannot parse message. ";
			if (e.getMessage() != null) {
				message += e.getMessage();
			}
			if (e.getLinkedException() != null && e.getLinkedException().getMessage() != null) {
				message += e.getLinkedException().getMessage();
			}
			
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, message);
		}
	}

}
