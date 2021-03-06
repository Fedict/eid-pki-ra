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
package be.fedict.eid.pkira.contracts;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;

import be.fedict.eid.pkira.generated.contracts.EIDPKIRAContractType;

import static be.fedict.eid.pkira.contracts.util.JAXBUtil.getMarshaller;
import static be.fedict.eid.pkira.contracts.util.JAXBUtil.getUnmarshaller;

/**
 * Client library to easily manipulate the XML contract documents.
 * 
 * @author Jan Van den Bergh
 */
public class EIDPKIRAContractsClient {

	public static final String NAME = "be.fedict.eid.pkira.wsclient.eidPKIRAContractsClient";

	private static final String ENCODING = "UTF8";
	private static final String NAMESPACE = "urn:be:fedict:eid:pkira:contracts";

	/**
	 * Marshals the document to a base64 string containing XML.
	 * 
	 * @param contractDocument
	 *            document to marshal.
	 * @return the base64 data.
	 * @throws XmlMarshallingException
	 *             when this fails.
	 */
	public <T extends EIDPKIRAContractType> String marshalToBase64(T contractDocument, Class<T> clazz)
			throws XmlMarshallingException {
		try {
			String xml = marshal(contractDocument, clazz);
			byte[] bytes = xml.getBytes(ENCODING);
			return Base64.encodeBase64String(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new XmlMarshallingException("Error encoding base64 data.", e);
		}
	}

	/**
	 * Marshals the document to XML.
	 * 
	 * @param contractDocument
	 *            document to marshal.
	 * @return the text in the XML.
	 * @throws XmlMarshallingException
	 *             when this fails.
	 */
	public <T extends EIDPKIRAContractType> String marshal(T contractDocument, Class<T> clazz)
			throws XmlMarshallingException {
		StringWriter writer = new StringWriter();
		marshal(contractDocument, clazz, writer);
		return writer.toString();
	}

	/**
	 * Marshals the document to XML.
	 * 
	 * @param contractDocument
	 *            document to marshal.
	 * @return the text in the XML.
	 * @throws XmlMarshallingException
	 *             when this fails.
	 */
	public <T extends EIDPKIRAContractType> void marshal(T contractDocument, Class<T> clazz, Writer writer)
			throws XmlMarshallingException {
		QName qname = new QName(NAMESPACE, getElementNameForType(clazz));
		JAXBElement<T> jaxbElement = new JAXBElement<T>(qname, clazz, contractDocument);

		try {
			getMarshaller().marshal(jaxbElement, writer);
		} catch (JAXBException e) {
			throw new XmlMarshallingException("Cannot marshal XML object.", e);
		}
	}

	/**
	 * Marshals the JAXB contract to a DOM document.
	 * 
	 * @param contractDocument
	 *            document to marshal.
	 * @return the text in the XML.
	 * @throws XmlMarshallingException
	 *             when this fails.
	 */
	public <T extends EIDPKIRAContractType> Document marshalToDocument(T contractDocument, Class<T> clazz)
			throws XmlMarshallingException {
		QName qname = new QName(NAMESPACE, getElementNameForType(clazz));
		JAXBElement<T> jaxbElement = new JAXBElement<T>(qname, clazz, contractDocument);

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document document = dbf.newDocumentBuilder().newDocument();

			getMarshaller().marshal(jaxbElement, document);

			return document;
		} catch (JAXBException e) {
			throw new XmlMarshallingException("Cannot marshal XML object.", e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Unmarshals an XML from a reader.
	 * 
	 * @param <T>
	 *            the type of document that is expected.
	 * @param reader
	 *            the reader to get the document from.
	 * @param clazz
	 *            the type of object.
	 * @return the unmarshalled object.
	 * @throws XmlMarshallingException
	 *             when this fails.
	 */
	@SuppressWarnings("unchecked")
	public <T extends EIDPKIRAContractType> T unmarshal(Reader reader, Class<T> clazz) throws XmlMarshallingException {
		Object unmarshalled;
		try {
			unmarshalled = getUnmarshaller().unmarshal(reader);
		} catch (JAXBException e) {
			throw new XmlMarshallingException("Cannot unmarshal XML data.", e);
		}

		if (unmarshalled != null && unmarshalled instanceof JAXBElement<?>) {
			JAXBElement<?> jaxbElement = (JAXBElement<?>) unmarshalled;
			if (jaxbElement.getDeclaredType() == clazz) {
				return (T) jaxbElement.getValue();
			} else {
				throw new XmlMarshallingException("XML contains an invalid element: "
						+ jaxbElement.getDeclaredType().getSimpleName() + ". Expected " + clazz.getSimpleName());
			}
		}

		throw new XmlMarshallingException("XML did not contain a valid element.");
	}

	/**
	 * Unmarshals an XML in a string.
	 * 
	 * @param <T>
	 *            the type of document that is expected.
	 * @param xml
	 *            the xml to parse.
	 * @param clazz
	 *            the type of object.
	 * @return the unmarshalled object.
	 * @throws XmlMarshallingException
	 *             when this fails.
	 */
	public <T extends EIDPKIRAContractType> T unmarshal(String xml, Class<T> clazz) throws XmlMarshallingException {
		if (xml == null) {
			throw new XmlMarshallingException("Xml is null.");
		}
		StringReader reader = new StringReader(xml);
		return unmarshal(reader, clazz);
	}

	/**
	 * Unmarshals an XML in a string.
	 * 
	 * @param <T>
	 *            the type of document that is expected.
	 * @param base64
	 *            the base64 encoded xml.
	 * @param clazz
	 *            the type of object.
	 * @return the unmarshalled object.
	 * @throws XmlMarshallingException
	 *             when this fails.
	 */
	public <T extends EIDPKIRAContractType> T unmarshalFromBase64(String base64, Class<T> clazz)
			throws XmlMarshallingException {
		if (base64 == null) {
			throw new XmlMarshallingException("Data is null.");
		}

		String xml;
		try {
			xml = new String(Base64.decodeBase64(base64), "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new XmlMarshallingException("Error decoding base64 data.", e);
		}
		StringReader reader = new StringReader(xml);
		return unmarshal(reader, clazz);
	}

	private <T> String getElementNameForType(Class<T> clazz) {
		String name = clazz.getSimpleName();
		return name.substring(0, name.length() - 4);
	}
}
