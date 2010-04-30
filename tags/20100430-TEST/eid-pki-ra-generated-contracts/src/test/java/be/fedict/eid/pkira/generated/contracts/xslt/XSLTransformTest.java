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
package be.fedict.eid.pkira.generated.contracts.xslt;

import static org.testng.Assert.assertTrue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.HTMLDocumentBuilder;
import org.custommonkey.xmlunit.TolerantSaxDocumentBuilder;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * @author Jan Van den Bergh
 */
public class XSLTransformTest {

	@Test
	public void testRequestXSL() throws Exception {
		testTransformation("/xslt/SigningExample.xml", "/xslt/SigningExample.html");
	}

	@Test
	public void testRevocationXSL() throws Exception {
		testTransformation("/xslt/RevocationExample.xml", "/xslt/RevocationExample.html");
	}

	private void testTransformation(String xmlResource, String htmlResource)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException,
			Exception {
		Source xsltSource = new StreamSource(XSLTransformTest.class
				.getResourceAsStream("/xslt/eid-pki-ra-view-contract.xsl"));
		Source xmlSource = new StreamSource(XSLTransformTest.class.getResourceAsStream(xmlResource));

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer(xsltSource);

		StringWriter writer = new StringWriter();
		transformer.transform(xmlSource, new StreamResult(writer));
		String html = writer.toString();
		System.out.println(html);

		Document testDocument = readHtmlDocument(new StringReader(html));

		Document controlDocument = readHtmlDocument(new InputStreamReader(XSLTransformTest.class
				.getResourceAsStream(htmlResource)));

		Diff diff = XMLUnit.compareXML(controlDocument, testDocument);
		assertTrue(diff.identical(), diff.toString());
	}

	private Document readHtmlDocument(Reader htmlReader) throws Exception {
		TolerantSaxDocumentBuilder tolerantSaxDocumentBuilder = new TolerantSaxDocumentBuilder(XMLUnit.newTestParser());
		HTMLDocumentBuilder htmlDocumentBuilder = new HTMLDocumentBuilder(tolerantSaxDocumentBuilder);
		Document document = htmlDocumentBuilder.parse(htmlReader);
		return document;

	}
}
