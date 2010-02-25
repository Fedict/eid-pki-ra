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
package be.fedict.eid.pkira.contracts;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;

public class EIDPKIRAContractsClientTest {

	private static final String XML_CSR = "CertificateSigningRequest.xml";
	private static final String XML_INVALID = "InvalidXml.notxml";

	@BeforeMethod
	public void setup() {
		XMLUnit.setIgnoreWhitespace(true);
	}

	@Test
	public void testMarshal() throws Exception {
		CertificateSigningRequestType request = CertificateSigningRequestBuilderTest
				.createFilledCertificateSigningRequest();

		String marshalled = new EIDPKIRAContractsClient().marshal(request, CertificateSigningRequestType.class);
		assertNotNull(marshalled);
		compareXmlData(marshalled, XML_CSR);
	}
	
	@Test(expectedExceptions=XmlMarshallingException.class)
	public void testMarshalInvalid() throws Exception {
		CertificateSigningRequestType request = new CertificateSigningRequestBuilder().toRequestType();

		new EIDPKIRAContractsClient().marshal(request, CertificateSigningRequestType.class);
	}
	
	@Test
	public void testUnmarshal() throws Exception {
		Reader fileReader = new InputStreamReader(getClass().getResourceAsStream(XML_CSR));
		
		CertificateSigningRequestType request = new EIDPKIRAContractsClient().unmarshal(fileReader, CertificateSigningRequestType.class);
		assertNotNull(request);
	}
	
	@Test(expectedExceptions=XmlMarshallingException.class)
	public void testUnmarshalInvalidXml() throws Exception {
		Reader fileReader = new InputStreamReader(getClass().getResourceAsStream(XML_INVALID));
		
		new EIDPKIRAContractsClient().unmarshal(fileReader, CertificateSigningRequestType.class);	
	}
	
	@Test(expectedExceptions=XmlMarshallingException.class)
	public void testUnmarshalInvalidType() throws Exception {
		Reader fileReader = new InputStreamReader(getClass().getResourceAsStream(XML_INVALID));
		
		new EIDPKIRAContractsClient().unmarshal(fileReader, CertificateRevocationRequestType.class);	
	}

	private void compareXmlData(String xml, String controlFileName) throws SAXException, IOException,
			ParserConfigurationException {
		InputStream resource = getClass().getResourceAsStream(controlFileName);
		InputStreamReader control = new InputStreamReader(resource);

		Diff diff = XMLUnit.compareXML(control, xml);
		assertTrue(diff.identical(), diff.toString());
	}

}
