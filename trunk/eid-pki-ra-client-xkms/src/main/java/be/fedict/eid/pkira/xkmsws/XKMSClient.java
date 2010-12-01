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

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3._2002._03.xkms_xbulk.BulkRegisterResultType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType;
import org.w3._2002._03.xkms_xbulk.RequestType;
import org.w3c.dom.Document;
import org.xkms.schema.xkms_2001_01_20.RegisterResult;

import be.fedict.eid.pkira.xkmsws.signing.XmlDocumentSigner;
import be.fedict.eid.pkira.xkmsws.util.HttpUtil;
import be.fedict.eid.pkira.xkmsws.util.RequestMessageCreator;
import be.fedict.eid.pkira.xkmsws.util.ResponseMessageParser;
import be.fedict.eid.pkira.xkmsws.util.XMLMarshallingUtil;

public class XKMSClient {

	public static final String PARAMETER_LOG_PREFIX = "logPrefix";

	private static final Log LOG = LogFactory.getLog(XKMSClient.class);
	private final HttpUtil httpUtil;
	private Document lastOutboundMessage;
	private final XMLMarshallingUtil marshallingUtil;
	private final Map<String, String> parameters;
	private final RequestMessageCreator requestMessageCreator;

	private final ResponseMessageParser responseMessageParser;

	private final XmlDocumentSigner xmlDocumentSigner;

	/**
	 * Creates an XKMSClient for the specific endpoint address using the
	 * parameters and optional handlers.
	 */
	public XKMSClient(String endpointAddress, Map<String, String> parameters) {
		LOG.info("Creating XKMSClient to " + endpointAddress);

		this.parameters = parameters;
		this.httpUtil = new HttpUtil(endpointAddress, parameters);
		this.marshallingUtil = new XMLMarshallingUtil();
		this.xmlDocumentSigner = new XmlDocumentSigner(parameters);
		this.requestMessageCreator = new RequestMessageCreator(parameters);
		this.responseMessageParser = new ResponseMessageParser(marshallingUtil);
	}

	/**
	 * Sends a request to the XKMS to generate a certificate.
	 * 
	 * @param csrData
	 *            byte array containing the CSR.
	 * @param validityInMonths
	 *            the validity of the certificate (in months)
	 * @param certificateType
	 *            the certificate type to generate (client, server, code)
	 * @return byte array containing the certificate.
	 * @throws XKMSClientException
	 *             if an error occurred while communicating to the XKMS service.
	 */
	public byte[] createCertificate(byte[] csrData, int validityInMonths, String certificateType)
			throws XKMSClientException {
		LOG.info("Creating certificate");

		// Create the request
		RequestType request = requestMessageCreator.createCSRRequestElement(csrData, validityInMonths, certificateType);

		// Execute it
		List<RegisterResult> results = executeRequest(request);
		if (results.size() != 1) {
			throw new XKMSClientException("Expected one result from the XKMS service, but got " + results.size());
		}
		RegisterResult result = results.get(0);

		// Parse the result
		return responseMessageParser.parseCreateCertificateResult(result);
	}

	public Document getLastOutboundMessage() {
		return lastOutboundMessage;
	}

	/**
	 * Sends a request to the XKMS to revoke a certificate.
	 * 
	 * @param serialNumber
	 *            serial number of the certificate to revoke.
	 * @param certificateType
	 *            the certificate type to revoke
	 * @throws XKMSClientException
	 *             if an error occurred while communicating to the XKMS service.
	 */
	public void revokeCertificate(BigInteger serialNumber, String certificateType) throws XKMSClientException {
		LOG.info("Revoking certificate");

		// Create the request
		RequestType request = requestMessageCreator.createRevocationElement(serialNumber, certificateType);

		// Execute it
		List<RegisterResult> results = executeRequest(request);
		if (results.size() != 1) {
			throw new XKMSClientException("Expected one result from the XKMS service, but got " + results.size());
		}

		// Parse the result
		responseMessageParser.parseRevokeCertificateResult(results.get(0));
	}

	/**
	 * Executes a bulk register requests for the specified request objects.
	 * 
	 * @param requestObjects
	 *            request objects.
	 * @return the result objects.
	 */
	private List<RegisterResult> executeRequest(RequestType... requestObjects) throws XKMSClientException {
		// Create and marshal the document
		BulkRegisterType registerRequest = requestMessageCreator.createBulkRegisterRequest(requestObjects);
		Document request = marshallingUtil.marshalBulkRegisterTypeToDocument(registerRequest);

		// Sign it
		if (parameters.containsKey(PARAMETER_LOG_PREFIX)) {
			marshallingUtil.writeDocumentToFile(request, parameters.get(PARAMETER_LOG_PREFIX) + "-unsigned.xml");
		}
		xmlDocumentSigner.signXKMSDocument(request, "BulkRegister", "SignedPart");
		if (parameters.containsKey(PARAMETER_LOG_PREFIX)) {
			marshallingUtil.writeDocumentToFile(request, parameters.get(PARAMETER_LOG_PREFIX) + "-signed.xml");
		}

		// Add soap headers
		marshallingUtil.addSoapHeaders(request);

		// Convert it to a string
		lastOutboundMessage = request;
		String requestMessage = marshallingUtil.convertDocumentToString(request);

		// Call the XKMS implementation
		byte[] responseMessage = httpUtil.postMessage(requestMessage);

		if (parameters.containsKey(PARAMETER_LOG_PREFIX)) {
			try {
				FileUtils.writeByteArrayToFile(new File(parameters.get(PARAMETER_LOG_PREFIX) + "-response.xml"),
						responseMessage);
			} catch (IOException e) {
				LOG.warn("Error", e);
			}
		}

		// Convert string to DOM document
		Document response = marshallingUtil.convertStringToDocument(responseMessage);

		// Remove SOAP headers
		marshallingUtil.removeSoapHeaders(response);

		// Parse the result
		BulkRegisterResultType result = marshallingUtil.unmarshalByteArrayToBulkRegisterResultType(response);
		return result.getSignedPart().getRegisterResults().getRegisterResult();
	}
}
