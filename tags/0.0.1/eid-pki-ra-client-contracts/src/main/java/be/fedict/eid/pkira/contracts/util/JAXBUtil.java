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
package be.fedict.eid.pkira.contracts.util;

import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import be.fedict.eid.pkira.contracts.CertificateRevocationRequestBuilder;
import be.fedict.eid.pkira.generated.contracts.EIDPKIRAContractType;
import be.fedict.eid.pkira.generated.contracts.ObjectFactory;

/**
 * Utility class to retrieve the JAXContext, Schema, marshallers and object
 * factories.
 * 
 * @author Jan Van den Bergh
 */
public class JAXBUtil {

	private static DatatypeFactory datatypeFactory;
	private static JAXBContext jaxbContext;
	private static ObjectFactory objectFactory = new ObjectFactory();
	private static Schema schema;

	/**
	 * Converts the data to an XMLGregorianCalendar in the GMT time zone.
	 */
	public static XMLGregorianCalendar createXmlGregorianCalendar(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));

		return getDatatypeFactory().newXMLGregorianCalendar(calendar);
	}

	/**
	 * Returns the data type factory to create exotic XML objects..
	 */
	public synchronized static DatatypeFactory getDatatypeFactory() {
		if (datatypeFactory != null) {
			return datatypeFactory;
		}

		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("Cannot get DatatypeFactory", e);
		}
		return datatypeFactory;
	}
	
	/**
	 * Returns the JAXBContext used for contracts.
	 */
	public static synchronized JAXBContext getJaxBContext() {
		if (jaxbContext != null) {
			return jaxbContext;
		}

		try {
			jaxbContext = JAXBContext.newInstance(EIDPKIRAContractType.class.getPackage().getName());
			return jaxbContext;
		} catch (JAXBException e) {
			throw new RuntimeException("Cannot create JAXBContext.", e);
		}
	}

	/**
	 * Returns a marshaller for contracts. This marshaller provides XML validation.
	 */
	public static Marshaller getMarshaller() {
		try {
			Marshaller marshaller = getJaxBContext().createMarshaller();
			marshaller.setSchema(getSchema());
			return marshaller;
		} catch (JAXBException e) {
			throw new RuntimeException("Error creating marshaller", e);
		}
	}

	/**
	 * Returns the object factory for contract documents.
	 */
	public synchronized static ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	/**
	 * Returns the parsed schema (based on the xsd).
	 */
	public synchronized static Schema getSchema() {
		if (schema != null) {
			return schema;
		}

		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL url = CertificateRevocationRequestBuilder.class.getResource("/xsd/eid-pki-ra.xsd");
		try {
			schema = schemaFactory.newSchema(url);
			return schema;
		} catch (SAXException e) {
			throw new RuntimeException("Cannot instantiate schema.", e);
		}
	}

	/**
	 * Returns an unmarshaller for contracts. This unmarshaller provides XML validation.
	 */
	public static Unmarshaller getUnmarshaller() {
		try {
			Unmarshaller unmarshaller = getJaxBContext().createUnmarshaller();
			unmarshaller.setSchema(getSchema());
			return unmarshaller;
		} catch (JAXBException e) {
			throw new RuntimeException("Error creating marshaller", e);
		}
	}

}
