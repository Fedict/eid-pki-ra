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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.jboss.seam.util.Base64;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import be.fedict.eid.pkira.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.contracts.ObjectFactory;
import be.fedict.eid.pkira.contracts.RequestType;
import be.fedict.eid.pkira.contracts.ResultType;

/**
 * Test for the Contract Parser.
 * 
 * @author Jan Van den Bergh
 */
public class ContractParserTest {

	private static final ObjectFactory FACTORY = new ObjectFactory();

	private static final String REQUEST_ID = "TEST-REQUEST-1";
	private static final String RESPONSE_ID = "TEST-RESPONSE-1";
	private static final String TEST_MESSAGE = "Test message";
	
	private ContractParserBean contractParser;

	@BeforeTest
	public void setup() {
		XMLUnit.setIgnoreWhitespace(true);
		contractParser = new ContractParserBean();
		contractParser.setupBean();
	}

	@Test
	public void testMarshalMessage() throws Exception {
		CertificateRevocationResponseType response = FACTORY.createCertificateRevocationResponseType();
		response.setRequestId(REQUEST_ID);
		response.setResponseId(RESPONSE_ID);
		response.setResult(ResultType.SUCCESS);
		response.setResultMessage(TEST_MESSAGE);

		String result = contractParser.marshalResponseMessage(FACTORY.createCertificateRevocationResponse(response));
		assertNotNull(result);

		compareXmlData(result, "CertificateRevocationResponse.xml");
	}

	@Test
	public void testUnmarshalEmptyData() {
		try {
			contractParser.unmarshalRequestMessage("", CertificateRevocationRequestType.class);
			fail("Expected exception.");
		} catch (ContractHandlerBeanException e) {
			assertEquals(e.getResultType(), ResultType.INVALID_MESSAGE);
		}
	}

	@Test(expectedExceptions = ContractHandlerBeanException.class)
	public void testUnmarshalIncompleteXml() throws ContractHandlerBeanException {
		testParseFile("IncompleteCertificateRevocationRequest.xml", CertificateRevocationRequestType.class);
	}

	@Test
	public void testUnmarshalInvalidData() {
		try {
			contractParser.unmarshalRequestMessage("(��)", CertificateRevocationRequestType.class);
			fail("Expected exception.");
		} catch (ContractHandlerBeanException e) {
			assertEquals(e.getResultType(), ResultType.INVALID_MESSAGE);
		}
	}

	@Test(expectedExceptions = ContractHandlerBeanException.class)
	public void testUnmarshalInvalidXml() throws ContractHandlerBeanException {
		testParseFile("InvalidCertificateRevocationRequest.xml", CertificateRevocationRequestType.class);
	}

	@Test
	public void testUnmarshalNull() {
		try {
			contractParser.unmarshalRequestMessage(null, CertificateSigningRequestType.class);
			fail("Expected ContractHandlerBeanException.");
		} catch (ContractHandlerBeanException e) {
			assertEquals(e.getResultType(), ResultType.INVALID_MESSAGE);
		}
	}

	@Test
	public void testUnmarshalRevocationRequest() throws ContractHandlerBeanException {
		CertificateRevocationRequestType request = testParseFile("CertificateRevocationRequest.xml",
				CertificateRevocationRequestType.class);
		assertNotNull(request);
	}

	private void compareXmlData(String base64data, String controlFileName) throws SAXException, IOException,
			ParserConfigurationException {
		InputStream resource = getClass().getClassLoader().getResourceAsStream(getResourceNameForXml(controlFileName));
		InputStreamReader control = new InputStreamReader(resource);
		InputStreamReader test = new InputStreamReader(new ByteArrayInputStream(Base64.decode(base64data)));

		Diff diff = XMLUnit.compareXML(control, test);
		assertTrue(diff.identical(), diff.toString());
	}

	private String getResourceNameForXml(String resourceName) {
		return "be/fedict/eid/blm/model/" + resourceName;
	}

	private <T extends RequestType> T testParseFile(String resourceName, Class<T> requestClass)
			throws ContractHandlerBeanException {
		URL resource = getClass().getClassLoader().getResource(getResourceNameForXml(resourceName));
		String fileName = resource.getFile();
		String base64data = Base64.encodeFromFile(fileName);
		return contractParser.unmarshalRequestMessage(base64data, requestClass);
	}
}
