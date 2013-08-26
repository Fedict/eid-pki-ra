/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
package be.fedict.eid.pkira.reports;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import be.fedict.eid.pkira.generated.reports.ObjectFactory;
import be.fedict.eid.pkira.generated.reports.ReportType;

/**
 * Client to simplify the user of the reports XSD.
 * 
 * @author Jan Van den Bergh
 */
public class ReportsClient {
	private static JAXBContext jaxbContext;

	/**
	 * Marshal a report to a string.
	 * 
	 * @param report
	 *            the report to marshal.
	 */
	public String marshalReport(ReportType report) {
		StringWriter writer = new StringWriter();

		try {
			JAXBElement<ReportType> jaxbElement = new ObjectFactory().createReport(report);
			getMarshaller().marshal(jaxbElement, writer);
		} catch (JAXBException e) {
			throw new RuntimeException("Cannot marshal XML object.", e);
		}

		return writer.toString();
	}
	
	/**
	 * Returns a marshaller for reports.
	 */
	public static Marshaller getMarshaller() {
		try {
			Marshaller marshaller = getJaxBContext().createMarshaller();
			return marshaller;
		} catch (JAXBException e) {
			throw new RuntimeException("Error creating marshaller", e);
		}
	}
	
	/**
	 * Returns the JAXBContext used for contracts.
	 */
	public static synchronized JAXBContext getJaxBContext() {
		if (jaxbContext != null) {
			return jaxbContext;
		}

		try {
			jaxbContext = JAXBContext.newInstance(ReportType.class.getPackage().getName());
			return jaxbContext;
		} catch (JAXBException e) {
			throw new RuntimeException("Cannot create JAXBContext.", e);
		}
	}
}
