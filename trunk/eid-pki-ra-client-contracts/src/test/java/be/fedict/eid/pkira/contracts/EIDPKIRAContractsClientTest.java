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

import org.apache.commons.codec.binary.Base64;
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

	private static final String BASE64_DATA = 
		"PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9I" +
		"nllcyI/PjxDZXJ0aWZpY2F0ZVNpZ25pbmdSZXF1ZXN0IHhtbG5zOm5zMj0iaHR0cD" +
		"ovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyIgeG1sbnM9InVybjpiZTpmZWR" +
		"pY3Q6ZWlkOnBraXJhOmNvbnRyYWN0cyI+PFJlcXVlc3RJZD5yZXF1ZXN0SWQ8L1Jl" +
		"cXVlc3RJZD48RGVzY3JpcHRpb24+ZGVzY3JpcHRpb248L0Rlc2NyaXB0aW9uPjxPc" +
		"GVyYXRvcj48TmFtZT5uYW1lPC9OYW1lPjxGdW5jdGlvbj5mdW5jdGlvbjwvRnVuY3" +
		"Rpb24+PFBob25lPnBob25lPC9QaG9uZT48L09wZXJhdG9yPjxMZWdhbE5vdGljZT5" +
		"sZWdhbE5vdGljZTwvTGVnYWxOb3RpY2U+PERpc3Rpbmd1aXNoZWROYW1lPmRuPC9E" +
		"aXN0aW5ndWlzaGVkTmFtZT48Q2VydGlmaWNhdGVUeXBlPkNsaWVudDwvQ2VydGlma" +
		"WNhdGVUeXBlPjxWYWxpZGl0eVBlcmlvZE1vbnRocz4xNTwvVmFsaWRpdHlQZXJpb2" +
		"RNb250aHM+PENTUj5jc3I8L0NTUj48L0NlcnRpZmljYXRlU2lnbmluZ1JlcXVlc3Q+";

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

	@Test
	public void testMarshalToBase64() throws Exception {
		CertificateSigningRequestType request = CertificateSigningRequestBuilderTest
				.createFilledCertificateSigningRequest();

		String marshalled = new EIDPKIRAContractsClient().marshalToBase64(request, CertificateSigningRequestType.class);
		assertNotNull(marshalled);
		compareXmlData(new String(Base64.decodeBase64(marshalled), "UTF8"), XML_CSR);
	}

	@Test(expectedExceptions = XmlMarshallingException.class)
	public void testMarshalInvalid() throws Exception {
		CertificateSigningRequestType request = new CertificateSigningRequestBuilder().toRequestType();

		new EIDPKIRAContractsClient().marshal(request, CertificateSigningRequestType.class);
	}

	@Test
	public void testUnmarshal() throws Exception {
		Reader fileReader = new InputStreamReader(getClass().getResourceAsStream(XML_CSR));

		CertificateSigningRequestType request = new EIDPKIRAContractsClient().unmarshal(fileReader,
				CertificateSigningRequestType.class);
		assertNotNull(request);
	}

	@Test(expectedExceptions = XmlMarshallingException.class)
	public void testUnmarshalInvalidXml() throws Exception {
		Reader fileReader = new InputStreamReader(getClass().getResourceAsStream(XML_INVALID));

		new EIDPKIRAContractsClient().unmarshal(fileReader, CertificateSigningRequestType.class);
	}

	@Test(expectedExceptions = XmlMarshallingException.class)
	public void testUnmarshalInvalidType() throws Exception {
		Reader fileReader = new InputStreamReader(getClass().getResourceAsStream(XML_INVALID));

		new EIDPKIRAContractsClient().unmarshal(fileReader, CertificateRevocationRequestType.class);
	}

	@Test
	public void testUnmarshalFromBase64() throws Exception {
		CertificateSigningRequestType request = new EIDPKIRAContractsClient().unmarshalFromBase64(BASE64_DATA,
				CertificateSigningRequestType.class);
		assertNotNull(request);
	}

	@Test(expectedExceptions = XmlMarshallingException.class)
	public void testUnmarshalFromBase64Invalid() throws Exception {
		new EIDPKIRAContractsClient().unmarshalFromBase64("9{!}df", CertificateSigningRequestType.class);
	}

	private void compareXmlData(String xml, String controlFileName) throws SAXException, IOException,
			ParserConfigurationException {
		InputStream resource = getClass().getResourceAsStream(controlFileName);
		InputStreamReader control = new InputStreamReader(resource);

		Diff diff = XMLUnit.compareXML(control, xml);
		assertTrue(diff.identical(), diff.toString());
	}

}
