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
package be.fedict.eid.blm.model.contracthandler;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import be.fedict.eid.pkira.contracts.CertificateRevocationResponseBuilder;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.RequestType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Test for the Contract Parser.
 * 
 * @author Jan Van den Bergh
 */
public class ContractParserTest {

	private static final String REQUEST_ID = "TEST-REQUEST-1";
	private static final String RESPONSE_ID = "TEST-RESPONSE-1";
	private static final String TEST_MESSAGE = "Test message";

	private ContractParserBean contractParser;

	@BeforeTest
	public void setup() {
		XMLUnit.setIgnoreWhitespace(true);
		contractParser = new ContractParserBean();
	}

	@Test
	public void testMarshalMessage() throws Exception {
		CertificateRevocationResponseType response = new CertificateRevocationResponseBuilder(RESPONSE_ID)
				.setRequestId(REQUEST_ID).setResult(ResultType.SUCCESS).setResultMessage(TEST_MESSAGE).toResponseType();

		String result = contractParser.marshalResponseMessage(response, CertificateRevocationResponseType.class);
		assertNotNull(result);

		compareXmlData(result, "CertificateRevocationResponse.xml");
	}

	@Test
	public void testUnmarshalRevocationRequest() throws ContractHandlerBeanException {
		CertificateRevocationRequestType request = testParseFile("CertificateRevocationRequest.xml",
				CertificateRevocationRequestType.class);
		assertNotNull(request);
	}

	private void compareXmlData(String base64data, String controlFileName) throws SAXException, IOException,
			ParserConfigurationException {
		String control = FileUtils.readFileToString(new File(getClass().getResource(controlFileName).getFile()));
		String test = new String(Base64.decodeBase64(base64data), "UTF-8");
		
		Diff diff = XMLUnit.compareXML(control, test);
		assertTrue(diff.identical(), diff.toString());
	}

	private <T extends RequestType> T testParseFile(String resourceName, Class<T> requestClass)
			throws ContractHandlerBeanException {
		try {
			URL resource = getClass().getResource(resourceName);
			byte[] data = FileUtils.readFileToByteArray(new File(resource.getFile()));
			String base64data = Base64.encodeBase64String(data);
			return contractParser.unmarshalRequestMessage(base64data, requestClass);
		} catch (IOException e) {
			fail("Could not read control XML", e);
			return null;
		}
	}
}
