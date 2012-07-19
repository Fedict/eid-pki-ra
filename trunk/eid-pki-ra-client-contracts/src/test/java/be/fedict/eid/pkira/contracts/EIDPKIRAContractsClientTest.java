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

	private static final String BASE64_DATA = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxv"
			+ "bmU9InllcyI/PjxDZXJ0aWZpY2F0ZVNpZ25pbmdSZXF1ZXN0IHhtbG5zOm5z"
			+ "Mj0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyIgeG1sbnM9"
			+ "InVybjpiZTpmZWRpY3Q6ZWlkOnBraXJhOmNvbnRyYWN0cyIgSWQ9InJlcXVl"
			+ "c3QiPjxSZXF1ZXN0SWQ+cmVxdWVzdElkPC9SZXF1ZXN0SWQ+PERlc2NyaXB0"
			+ "aW9uPmRlc2NyaXB0aW9uPC9EZXNjcmlwdGlvbj48T3BlcmF0b3I+PE5hbWU+"
			+ "bmFtZTwvTmFtZT48RnVuY3Rpb24+ZnVuY3Rpb248L0Z1bmN0aW9uPjxQaG9u"
			+ "ZT5waG9uZTwvUGhvbmU+PC9PcGVyYXRvcj48TGVnYWxOb3RpY2U+bGVnYWxO"
			+ "b3RpY2U8L0xlZ2FsTm90aWNlPjxEaXN0aW5ndWlzaGVkTmFtZT5kbjwvRGlz"
			+ "dGluZ3Vpc2hlZE5hbWU+PENlcnRpZmljYXRlVHlwZT5DbGllbnQ8L0NlcnRp"
			+ "ZmljYXRlVHlwZT48VmFsaWRpdHlQZXJpb2RNb250aHM+MTU8L1ZhbGlkaXR5"
			+ "UGVyaW9kTW9udGhzPjxDU1I+Y3NyPC9DU1I+PC9DZXJ0aWZpY2F0ZVNpZ25p" + "bmdSZXF1ZXN0Pg==";;

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
