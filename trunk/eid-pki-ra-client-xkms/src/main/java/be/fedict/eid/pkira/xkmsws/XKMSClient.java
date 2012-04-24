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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3._2002._03.xkms_xbulk.BulkRegisterResultType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType;
import org.w3._2002._03.xkms_xbulk.RequestType;
import org.w3c.dom.Document;
import org.xkms.schema.xkms_2001_01_20.RegisterResult;

import be.fedict.eid.pkira.xkmsws.XKMSLogger.XKMSMessageType;
import be.fedict.eid.pkira.xkmsws.signing.XkmsXmlDocumentSigner;
import be.fedict.eid.pkira.xkmsws.util.HttpUtil;
import be.fedict.eid.pkira.xkmsws.util.RequestMessageCreator;
import be.fedict.eid.pkira.xkmsws.util.ResponseMessageParser;
import be.fedict.eid.pkira.xkmsws.util.XMLMarshallingUtil;

public class XKMSClient {

	private static interface RegisterResultParser<OutputType> {
		OutputType parseResponse(RegisterResult registerResult) throws XKMSClientException;
	}

	public static final String PARAMETER_LOG_PREFIX = "xkms.logPrefix";

	private static final Log LOG = LogFactory.getLog(XKMSClient.class);
	private final HttpUtil httpUtil;
	private Document lastOutboundMessage;
	private final XMLMarshallingUtil marshallingUtil;
	private final Map<String, String> parameters;
	private final RequestMessageCreator requestMessageCreator;

	private final ResponseMessageParser responseMessageParser;

	private final XkmsXmlDocumentSigner xmlDocumentSigner;

	private XKMSLogger xkmsLogger;

	/**
	 * Creates an XKMSClient for the specific endpoint address using the
	 * parameters and optional handlers.
	 */
	public XKMSClient(String endpointAddress, Map<String, String> parameters) {
		LOG.info("Creating XKMSClient to " + endpointAddress);

		this.parameters = parameters;
		this.httpUtil = new HttpUtil(endpointAddress, parameters);
		this.marshallingUtil = new XMLMarshallingUtil();
		this.xmlDocumentSigner = new XkmsXmlDocumentSigner(parameters);
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
	 *            the certificate type to generate (client, server, code,
	 *            persons)
	 * @param timeShiftSeconds 
	 * @return byte array containing the certificate.
	 * @throws XKMSClientException
	 *             if an error occurred while communicating to the XKMS service.
	 */
	public byte[] createCertificate(byte[] csrData, int validityInMonths, String certificateType, int timeShiftSeconds)
			throws XKMSClientException {
		LOG.info("Creating certificate");

		// Create the request
		RequestType request = requestMessageCreator.createCSRRequestElement(csrData, validityInMonths, certificateType, timeShiftSeconds);

		// Execute it
		return executeRequest(XKMSMessageType.REQUEST, request, new RegisterResultParser<byte[]>() {
			@Override
			public byte[] parseResponse(RegisterResult registerResult) throws XKMSClientException {
				return responseMessageParser.parseCreateCertificateResult(registerResult);
			}
		});
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
		executeRequest(XKMSMessageType.REVOCATION, request, new RegisterResultParser<Object>() {
			@Override
			public byte[] parseResponse(RegisterResult registerResult) throws XKMSClientException {
				responseMessageParser.parseRevokeCertificateResult(registerResult);
				return null;
			}
		});
	}

	/**
	 * Executes a bulk register requests for the specified request objects.
	 * 
	 * @param requestObjects
	 *            request objects.
	 * @return the result objects.
	 */
	private <OutputType> OutputType executeRequest(XKMSMessageType messageType, RequestType requestObject, RegisterResultParser<OutputType> parser) throws XKMSClientException {
		String requestMessage = null;
		byte[] responseMessage = null;
		try {
			// Create and marshal the document
			BulkRegisterType registerRequest = requestMessageCreator.createBulkRegisterRequest(requestObject);
			Document request = marshallingUtil.marshalBulkRegisterTypeToDocument(registerRequest);

			// Sign it
			if (parameters.containsKey(PARAMETER_LOG_PREFIX)) {
				marshallingUtil.writeDocumentToFile(request, parameters.get(PARAMETER_LOG_PREFIX), "-unsigned.xml");
			}
			xmlDocumentSigner.signXKMSDocument(request, "BulkRegister", "SignedPart");
			if (parameters.containsKey(PARAMETER_LOG_PREFIX)) {
				marshallingUtil.writeDocumentToFile(request, parameters.get(PARAMETER_LOG_PREFIX), "-signed.xml");
			}

			// Add soap headers
			marshallingUtil.addSoapHeaders(request);

			// Convert it to a string
			lastOutboundMessage = request;
			requestMessage = marshallingUtil.convertDocumentToString(request);

			// Call the XKMS implementation
			responseMessage = httpUtil.postMessage(requestMessage);
			if (parameters.containsKey(PARAMETER_LOG_PREFIX)) {
				marshallingUtil.writeDocumentToFile(responseMessage, parameters.get(PARAMETER_LOG_PREFIX),
						"-response.xml");
			}

			// Convert string to DOM document
			Document response = marshallingUtil.convertStringToDocument(responseMessage);

			// Remove SOAP headers
			marshallingUtil.removeSoapHeaders(response);

			// Parse the result
			BulkRegisterResultType result = marshallingUtil.unmarshalByteArrayToBulkRegisterResultType(response);

			List<RegisterResult> results = result.getSignedPart().getRegisterResults().getRegisterResult();
			if (results.size() != 1) {
				throw new XKMSClientException("Expected one result from the XKMS service, but got " + results.size());
			}

			OutputType output = parser.parseResponse(results.get(0));
			
			if (xkmsLogger != null) {
				xkmsLogger.logSuccesfulInteraction(messageType, requestMessage, responseMessage);
			}
			
			return output;
		} catch (XKMSClientException e) {
			if (xkmsLogger != null) {
				xkmsLogger.logError(messageType, requestMessage, responseMessage, e);
			}
			throw e;
		} catch (Throwable t) {
			if (xkmsLogger != null) {
				xkmsLogger.logError(messageType, requestMessage, responseMessage, t);
			}
			throw new XKMSClientException("Unexpected error.", t);
		}
	}

	public void setXkmsLogger(XKMSLogger xkmsLogger) {
		this.xkmsLogger = xkmsLogger;
	}
}
