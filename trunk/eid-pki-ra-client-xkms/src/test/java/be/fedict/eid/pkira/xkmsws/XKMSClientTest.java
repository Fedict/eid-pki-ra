/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
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

package be.fedict.eid.pkira.xkmsws;

import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jboss.seam.log.Logging;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import be.fedict.eid.pki.ra.xkms.ws.MockXKMSWebService;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CSRParserImpl;
import be.fedict.eid.pkira.xkmsws.keyinfo.SigningKeyKeyStoreProvider;
import be.fedict.eid.pkira.xkmsws.keyinfo.SigningKeyKeyStoreProviderTest;

public class XKMSClientTest {

	public static final String URL = "http://localhost:33333/xkms";

	private static final String[] ACCEPTED_DIFFERENCES_CREATE_CERTIFICATE =
		{
				"/Envelope[1]/Body[1]/BulkRegister[1]/SignedPart[1]/BatchHeader[1]/BatchID[1]/text()[1]",
				"/Envelope[1]/Body[1]/BulkRegister[1]/SignedPart[1]/BatchHeader[1]/BatchTime[1]/text()[1]",
				"/Envelope[1]/Body[1]/BulkRegister[1]/SignedPart[1]/Requests[1]/Request[1]/KeyID[1]/text()[1]",
				"/Envelope[1]/Body[1]/BulkRegister[1]/SignedPart[1]/Requests[1]/Request[1]/ProcessInfo[1]/AttributeCertificate[1]/ValidityInterval[1]/NotBefore[1]/text()[1]",
				"/Envelope[1]/Body[1]/BulkRegister[1]/SignedPart[1]/Requests[1]/Request[1]/ProcessInfo[1]/AttributeCertificate[1]/ValidityInterval[1]/NotAfter[1]/text()[1]",
				"/Envelope[1]/Body[1]/BulkRegister[1]/Signature[1]/SignedInfo[1]/Reference[1]/DigestValue[1]/text()[1]",
				"/Envelope[1]/Body[1]/BulkRegister[1]/Signature[1]/SignatureValue[1]/text()[1]", };

	private Endpoint webServiceEndpoint;
	private MessageInterceptionHandler messageInterceptionHandler;
	private XKMSClient xkmsClient;

	private Map<String, String> parameters = new HashMap<String, String>();

	@BeforeSuite
	public void startTestWebService() {
		webServiceEndpoint = Endpoint.create(new MockXKMSWebService());
		webServiceEndpoint.publish(URL);
	}

	@AfterSuite
	public void stopTestWebService() {
		webServiceEndpoint.stop();
	}

	@SuppressWarnings("unchecked")
	@BeforeMethod
	public void setup() {		
		// Instantiate client
		parameters.put(XKMSClient.PARAMETER_BUC, "8047651269");
		parameters.put(XKMSSignatureSOAPHandler.PARAMETER_SIGNING_KEY_PROVIDER_CLASS, SigningKeyKeyStoreProvider.class
				.getName());
		String url = SigningKeyKeyStoreProviderTest.class.getResource("/test.jks").toExternalForm();
		parameters.put(SigningKeyKeyStoreProvider.PARAMETER_KEYSTORE_URL, url);
		parameters.put(SigningKeyKeyStoreProvider.PARAMETER_KEYSTORE_ENTRYNAME, "test");
		parameters.put(SigningKeyKeyStoreProvider.PARAMETER_KEYSTORE_PASSWORD, "changeit");
		parameters.put(SigningKeyKeyStoreProvider.PARAMETER_KEYSTORE_ENTRY_PASSWORD, "changeit");
		
		messageInterceptionHandler = new MessageInterceptionHandler();
		
		xkmsClient = new XKMSClient(URL, parameters, messageInterceptionHandler);

		// Configure XML Unit
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}

	@Test
	public void testCreateCertificate() throws Exception {
		// Create CSR der to use
		String csrPem = readResource("/valid.csr");
		byte[] csrDer = createCSRParser().parseCSR(csrPem).getDerEncoded();

		// Call the certificate creation
		byte[] certificate = xkmsClient.createCertificate(csrDer, 15);
		assertNotNull(certificate);

		// Validate the outgoing message
		Document controlDocument = XMLUnit.buildControlDocument(readResource("/exampleSigningRequest.xml"));
		Document testDocument = messageInterceptionHandler.getLastOutboundMessage().getOwnerDocument();
		Diff diff = XMLUnit.compareXML(controlDocument, testDocument);
		diff = new IgnoreLocationsDifferenceListener(diff, ACCEPTED_DIFFERENCES_CREATE_CERTIFICATE);

		XMLAssert.assertXMLIdentical(diff, true);
	}
	
	@Test
	public void testRevokeCertificate() throws Exception {
		xkmsClient.revokeCertificate(BigInteger.valueOf(123L));
	}
	
	@Test
	public void testRevokeCertificateAlreadyRevoked() throws Exception {
		xkmsClient.revokeCertificate(BigInteger.valueOf(1001L));
	}
	
	@Test(expectedExceptions=XKMSClientException.class)
	public void testRevokeCertificateAlreadyError() throws Exception {
		xkmsClient.revokeCertificate(BigInteger.valueOf(1002L));
	}

	private String readResource(String resourceName) throws IOException {
		String result = "";
		
		InputStream inputStream = XKMSClientTest.class.getResourceAsStream(resourceName);
		byte[] inputBytes = new byte[8192];
		int read;				
		while (-1 != (read=inputStream.read(inputBytes))) {
			result += new String(inputBytes, 0, read);
		}
		
		return result;
	}

	private CSRParser createCSRParser() {
		CSRParserImpl csrParser = new CSRParserImpl();
		csrParser.setLog(Logging.getLog(XKMSClientTest.class));
		return csrParser;
	}
}
