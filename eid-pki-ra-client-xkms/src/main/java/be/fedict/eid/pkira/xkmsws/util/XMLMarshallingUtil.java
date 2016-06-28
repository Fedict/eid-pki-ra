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

package be.fedict.eid.pkira.xkmsws.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3._2002._03.xkms_xbulk.BulkRegisterResultType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import be.fedict.eid.pkira.xkmsws.XKMSClientException;

public class XMLMarshallingUtil {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private static final String NAMESPACE_SOAP = "http://schemas.xmlsoap.org/soap/envelope/";
	private final DocumentBuilder documentBuilder;
	private final JAXBContext jaxbContext;

	public XMLMarshallingUtil() {
		// Initialze JAXBContext
		try {
			jaxbContext = JAXBContext.newInstance(BulkRegisterType.class.getPackage().getName());
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		// Initialize document builder
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			documentBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public void addSoapHeaders(Document document) {
		Element envelope = document.createElementNS(NAMESPACE_SOAP, "Envelope");
		Element body = document.createElementNS(NAMESPACE_SOAP, "Body");
		envelope.appendChild(body);
		body.appendChild(document.getDocumentElement());
		document.appendChild(envelope);
	}

	public String convertDocumentToString(Document requestMessage) {
		Source source = new DOMSource(requestMessage);

		StringWriter writer = new StringWriter();
		StreamResult streamResult = new StreamResult(writer);

		try {
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, streamResult);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}

		return writer.toString();
	}

	public Document convertStringToDocument(byte[] message) throws XKMSClientException {
		try {
			return documentBuilder.parse(new ByteArrayInputStream(message));
		} catch (SAXException e) {
			throw new XKMSClientException("Error parsing response message.", e);
		} catch (IOException e) {
			throw new XKMSClientException("Error parsing response message.", e);
		}
	}

	/**
	 * Extracts the first element from the list matching the type.
	 *
	 * @param name
	 *            name of the element to get.
	 * @param list
	 *            list with either this type of a JAXBElement with this type.
	 * @return the first matching element.
	 */
	public Object getFromJAXBElementList(List<Object> list, String name) {
		for (Object object : list) {
			if (object instanceof JAXBElement<?>) {
				JAXBElement<?> element = (JAXBElement<?>) object;
				if (name.equals(element.getName().getLocalPart())) {
					return element.getValue();
				}
			} else if (object instanceof Element) {
				Element element = (Element) object;
				if (name.equals(element.getLocalName())) {
					return element.getTextContent().trim();
				}
			}
		}

		return null;
	}

	/**
	 * Extracts the first element from the list matching the type.
	 *
	 * @param <T>
	 *            expected type.
	 * @param clazz
	 *            expected type.
	 * @param list
	 *            list with either this type of a JAXBElement with this type.
	 * @return the first matching element.
	 */
	public <T> T getFromList(Class<T> clazz, List<Object> list) {
		for (Object object : list) {
			if (clazz.isInstance(object)) {
				return clazz.cast(object);
			}
			if (object instanceof JAXBElement<?>) {
				JAXBElement<?> jaxbElement = (JAXBElement<?>) object;
				if (jaxbElement.getDeclaredType().equals(clazz)) {
					return clazz.cast(jaxbElement.getValue());
				}
			}
		}

		return null;
	}

	public Document marshalBulkRegisterTypeToDocument(BulkRegisterType bulkRegisterType) throws XKMSClientException {
		QName qname = new QName("http://www.w3.org/2002/03/xkms-xbulk", "BulkRegister");
		JAXBElement<BulkRegisterType> jaxbElement = new JAXBElement<BulkRegisterType>(qname, BulkRegisterType.class,
				bulkRegisterType);

		try {
			Document doc = documentBuilder.newDocument();
			Marshaller marshaller = jaxbContext.createMarshaller();

			try {
				Object mapper = new com.sun.xml.bind.marshaller.NamespacePrefixMapper() {
					@Override
					public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
						if (namespaceUri.equals("http://www.w3.org/2002/03/xkms-xbulk")) {
							return "xbulk";
						}
						if (namespaceUri.equals("http://www.xkms.org/schema/xkms-2001-01-20")) {
							return "xkms";
						}
						if (namespaceUri.equals("http://www.w3.org/2000/09/xmldsig#")) {
							return "ds";
						}
						if (namespaceUri.equals("http://xkms.ubizen.com/kitoshi")) {
							return "ki";
						}

						throw new RuntimeException("Unknown namespace " + namespaceUri);
					}
				};
				marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			marshaller.marshal(jaxbElement, doc);

			return doc;
		} catch (JAXBException e) {
			throw new XKMSClientException("Cannot marshal message.", e);
		}
	}

	public void removeSoapHeaders(Document document) {
		NodeList bodyNodes = document.getElementsByTagNameNS(NAMESPACE_SOAP, "Body");
		if (bodyNodes != null && bodyNodes.getLength() == 1) {
			Element body = (Element) bodyNodes.item(0);

			NodeList children = body.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof Element) {
					document.removeChild(document.getDocumentElement());
					document.appendChild(children.item(i));
					break;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public BulkRegisterResultType unmarshalByteArrayToBulkRegisterResultType(Document response)
			throws XKMSClientException {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<BulkRegisterResultType> unmarshalled = (JAXBElement<BulkRegisterResultType>) unmarshaller
					.unmarshal(response);

			return unmarshalled.getValue();
		} catch (JAXBException e) {
			throw new XKMSClientException("Cannot unmarshal message.", e);
		}
	}

	public void writeDocumentToFile(byte[] responseMessage, String prefix, String suffix) {
		String fileName = createFileName(prefix, suffix);
		try {
			FileUtils.writeByteArrayToFile(new File(fileName), responseMessage);
		} catch (IOException e) {
			System.err.println("Error writing XML document.");
			e.printStackTrace();
		}
	}

	public void writeDocumentToFile(Document doc, String prefix, String suffix) {
		// Prepare the DOM document for writing
		Source source = new DOMSource(doc);
		Result result = new StreamResult(new File(createFileName(prefix, suffix)));
		// Write the DOM document to the file
		try {
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, result);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	private String createFileName(String prefix, String suffix) {
		return prefix + DATE_FORMAT.format(new Date()) + suffix;
	}

}
