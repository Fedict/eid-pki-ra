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
import java.net.URL;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3._2000._09.xmldsig_.KeyInfoType;
import org.w3._2000._09.xmldsig_.X509DataType;
import org.w3._2002._03.xkms_xbulk.BatchHeaderType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterResultType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType.SignedPart;
import org.w3._2002._03.xkms_xbulk.ProcessInfoType;
import org.w3._2002._03.xkms_xbulk.RequestType;
import org.w3._2002._03.xkms_xbulk.RequestsType;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSService;
import org.xkms.schema.xkms_2001_01_20.AssertionStatus;
import org.xkms.schema.xkms_2001_01_20.KeyBindingType;
import org.xkms.schema.xkms_2001_01_20.RegisterResult;
import org.xkms.schema.xkms_2001_01_20.RegisterResult.Answer;
import org.xkms.schema.xkms_2001_01_20.Respond;
import org.xkms.schema.xkms_2001_01_20.ResultCode;

import com.ubizen.og.xkms.schema.xkms_2003_09.AttributeCertificate;
import com.ubizen.og.xkms.schema.xkms_2003_09.ValidityIntervalType;

public class XKMSClient {

	public static final String KEYNAME_REQUEST = "http://xkms.og.ubizen.com/keyname?buc_id={0}";
	// public static final String KEYNAME_REVOKE =
	// "http://xkms.og.ubizen.com/keyname?buc_id={0}&cert_serialnumber={1}";
	public static final String KEYNAME_REVOKE = "http://xkms.og.ubizen.com/keyname?cert_serialnumber={1}";
	public static final String NAMESPACE_URI = "http://www.w3.org/2002/03/xkms-xbulk#wsdl";
	public static final String SERVICE_NAME = "XKMSService";
	public static final String WSDL_RESOURCE = "/wsdl/xkms2-xbulk.wsdl";

	private static final Log LOG = LogFactory.getLog(XKMSClient.class);

	private static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone("Z");
	public static final String PARAMETER_BUC = "buc";

	private final DatatypeFactory datatypeFactory;
	private final String endpointAddress;
	private final SOAPHandler<SOAPMessageContext>[] extraHandlers;

	private final com.ubizen.og.xkms.schema.xkms_2003_09.ObjectFactory ogcmObjectFactory = new com.ubizen.og.xkms.schema.xkms_2003_09.ObjectFactory();
	private final Map<String, String> parameters;
	private final org.w3._2002._03.xkms_xbulk.ObjectFactory xbulkObjectFactory = new org.w3._2002._03.xkms_xbulk.ObjectFactory();
	private final org.xkms.schema.xkms_2001_01_20.ObjectFactory xkmsObjectFactory = new org.xkms.schema.xkms_2001_01_20.ObjectFactory();
	private final org.w3._2000._09.xmldsig_.ObjectFactory xmldsigObjectFactory = new org.w3._2000._09.xmldsig_.ObjectFactory();

	/**
	 * Creates an XKMSClient for the specific endpoint address using the
	 * parameters and optional handlers.
	 */
	public XKMSClient(String endpointAddress, Map<String, String> parameters,
			SOAPHandler<SOAPMessageContext>... extraHandlers) {
		LOG.info("Creating XKMSClient to " + endpointAddress);

		this.endpointAddress = endpointAddress;
		this.parameters = parameters;
		this.extraHandlers = extraHandlers;

		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
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
		RequestType request = createCSRRequestElement(csrData, validityInMonths, certificateType);

		// Execute it
		List<RegisterResult> results = executeRequest(request);
		if (results.size() != 1) {
			throw new XKMSClientException("Expected one result from the XKMS service, but got " + results.size());
		}
		RegisterResult result = results.get(0);

		// Parse the result
		return parseCreateCertificateResult(result);
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
		RequestType request = createRevocationElement(serialNumber, certificateType);

		// Execute it
		List<RegisterResult> results = executeRequest(request);
		if (results.size() != 1) {
			throw new XKMSClientException("Expected one result from the XKMS service, but got " + results.size());
		}
		// Parse the result
		parseRevokeCertificateResult(results.get(0));
	}

	/**
	 * Creates a bulk register request containing the specified request objects.
	 */
	private BulkRegisterType createBulkRegisterRequest(RequestType... requestObjects) {
		// Create the signed part
		SignedPart signedPart1 = xbulkObjectFactory.createBulkRegisterTypeSignedPart();
		String signedPartId = "signed-part";
		signedPart1.setId(signedPartId);
		SignedPart signedPart = signedPart1;

		// Add the respond element to it
		Respond respond = xkmsObjectFactory.createRespond();
		respond.getString().add("RetrievalMethod");
		respond.getString().add("X509Cert");
		// respond.getString().add("X509Chain");
		signedPart.setRespond(respond);

		// Add the batch header to it
		BatchHeaderType batchHeader = xbulkObjectFactory.createBatchHeaderType();

		String batchId = "batch-id-" + UUID.randomUUID().toString();
		batchHeader.getBatchIDAndBatchTimeAndNumberOfRequests().add(batchId);

		GregorianCalendar batchTime = new GregorianCalendar();
		batchTime.setTimeZone(TIMEZONE_GMT);
		batchHeader.getBatchIDAndBatchTimeAndNumberOfRequests().add(datatypeFactory.newXMLGregorianCalendar(batchTime));

		BigInteger numRequests = BigInteger.valueOf(requestObjects.length);
		batchHeader.getBatchIDAndBatchTimeAndNumberOfRequests().add(numRequests);

		signedPart.setBatchHeader(batchHeader);

		// Add the requests to it
		RequestsType requests = xbulkObjectFactory.createRequestsType();
		signedPart.setRequests(requests);
		for (RequestType requestObject : requestObjects) {
			requests.getRequest().add(requestObject);
		}
		requests.setNumber(Integer.toString(requestObjects.length));

		// Create the bulk register element
		BulkRegisterType register = xbulkObjectFactory.createBulkRegisterType();
		register.setSignedPart(signedPart);

		return register;
	}

	/**
	 * Creates a bulk XKMS request for a CSR.
	 * 
	 * @param certificateType
	 */
	private RequestType createCSRRequestElement(byte[] csrData, int validityInMonths, String certificateType) {
		// Create the request
		RequestType request = xbulkObjectFactory.createRequestType();
		request.setKeyID("key-id-" + UUID.randomUUID().toString());

		// Add the key info
		KeyInfoType keyInfo = xmldsigObjectFactory.createKeyInfoType();
		request.setKeyInfo(keyInfo);

		// Add the key name
		String keyName = MessageFormat.format(KEYNAME_REQUEST, encode(getBuc(certificateType)));
		keyInfo.getContent().add(xmldsigObjectFactory.createKeyName(keyName));

		// Add the CSR
		JAXBElement<byte[]> pkcs10Element = xbulkObjectFactory.createPKCS10(csrData);
		keyInfo.getContent().add(pkcs10Element);

		// Add the process info
		ProcessInfoType processInfo = xbulkObjectFactory.createProcessInfoType();
		request.setProcessInfo(processInfo);

		// Add the publish parameter
		processInfo.getReasonAndReasonCodeAndPublish().add(Boolean.TRUE);

		// Add the attribute certificate
		AttributeCertificate attributeCertificate = ogcmObjectFactory.createAttributeCertificate();
		processInfo.getReasonAndReasonCodeAndPublish().add(attributeCertificate);

		// Add the validity interval
		ValidityIntervalType validityInterval = ogcmObjectFactory.createValidityIntervalType();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TIMEZONE_GMT);
		validityInterval.setNotBefore(datatypeFactory.newXMLGregorianCalendar(calendar));
		calendar.add(Calendar.MONTH, validityInMonths);
		validityInterval.setNotAfter(datatypeFactory.newXMLGregorianCalendar(calendar));
		attributeCertificate.setValidityInterval(validityInterval);

		// Add the status
		request.setStatus(AssertionStatus.INDETERMINATE);

		return request;
	}

	private String getBuc(String certificateType) {
		return parameters.get(PARAMETER_BUC + "." + certificateType.toLowerCase());
	}

	/**
	 * Creates a bulk XKMS request for a revocation.
	 */
	private RequestType createRevocationElement(BigInteger serialNumber, String certificateType) {
		// Create the request
		RequestType request = xbulkObjectFactory.createRequestType();
		request.setKeyID("key-id-" + UUID.randomUUID().toString());
		request.setStatus(AssertionStatus.INVALID);

		// Add the key info
		KeyInfoType keyInfo = xmldsigObjectFactory.createKeyInfoType();
		request.setKeyInfo(keyInfo);

		// Add the key name
		String keyName = MessageFormat.format(KEYNAME_REVOKE,
				encode(parameters.get(PARAMETER_BUC + "." + certificateType.toLowerCase())),
				encode(serialNumber.toString()));
		keyInfo.getContent().add(xmldsigObjectFactory.createKeyName(keyName));

		return request;

	}

	/**
	 * Encodes a parameter in the key name.
	 */
	private String encode(String toEncode) {
		if (toEncode == null) {
			return "";
		}

		final String ENCODED = ",+\"\\<>;&";

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < toEncode.length(); i++) {
			char ch = toEncode.charAt(i);
			if (ENCODED.indexOf(ch) != -1) {
				result.append('\\');
			}
			result.append(ch);
		}

		return result.toString();
	}

	/**
	 * Executes a bulk register requests for the specified request objects.
	 * 
	 * @param requestObjects
	 *            request objects.
	 * @return the result objects.
	 */
	private List<RegisterResult> executeRequest(RequestType... requestObjects) throws XKMSClientException {
		BulkRegisterType registerRequest = createBulkRegisterRequest(requestObjects);
		BulkRegisterResultType registerResult = getXKMSPort().bulkRegister(registerRequest);

		if (registerResult == null || registerResult.getSignedPart() == null
				|| registerResult.getSignedPart().getRegisterResults() == null) {
			throw new XKMSClientException("Registration results are missing in the XKMS response");
		}

		return registerResult.getSignedPart().getRegisterResults().getRegisterResult();
	}

	/**
	 * Extracts the first element from the list matching the type.
	 * 
	 * @param <T>
	 *            expected type.
	 * @param clazz
	 *            expected type.
	 * @param list
	 *            list with either this type of a JAXBElement with this type.
	 * @return the first matching element.
	 */
	private Object getFromJAXBElementList(List<Object> list, String name) {
		for (Object object : list) {
			JAXBElement<?> element = (JAXBElement<?>) object;
			if (name.equals(element.getName().getLocalPart())) {
				return element.getValue();
			}
		}

		return null;
	}

	/**
	 * Extracts the first element from the list matching the type.
	 * 
	 * @param <T>
	 *            expected type.
	 * @param clazz
	 *            expected type.
	 * @param list
	 *            list with either this type of a JAXBElement with this type.
	 * @return the first matching element.
	 */
	private <T> T getFromList(Class<T> clazz, List<Object> list) {
		for (Object object : list) {
			if (clazz.isInstance(object)) {
				return clazz.cast(object);
			}
			if (object instanceof JAXBElement<?>) {
				JAXBElement<?> jaxbElement = (JAXBElement<?>) object;
				if (jaxbElement.getDeclaredType().equals(clazz)) {
					return clazz.cast(jaxbElement.getValue());
				}
			}
		}

		return null;
	}

	/**
	 * Get the key binding, handling errors (missing element).
	 */
	private KeyBindingType getKeyBinding(RegisterResult result) throws XKMSClientException {
		Answer answer = result.getAnswer();
		if (answer == null || answer.getKeyBinding() == null || answer.getKeyBinding().size() == 0) {
			throw new XKMSClientException("No key info found in the reply from XKMS.");
		}

		KeyBindingType keyBinding = answer.getKeyBinding().get(0);
		return keyBinding;
	}

	/**
	 * Get the result reason from the OGCM parameters.
	 */
	private String getResultReason(KeyBindingType keyBinding) {
		if (keyBinding == null || keyBinding.getProcessInfo() == null || keyBinding.getProcessInfo().getAny() == null) {
			return null;
		}

		return (String) getFromJAXBElementList(keyBinding.getProcessInfo().getAny(), "Reason");
	}

	/**
	 * Get the result reason code from the OGCM parameters.
	 */
	private BigInteger getResultReasonCode(KeyBindingType keyBinding) {
		if (keyBinding == null || keyBinding.getProcessInfo() == null || keyBinding.getProcessInfo().getAny() == null) {
			return null;
		}

		return (BigInteger) getFromJAXBElementList(keyBinding.getProcessInfo().getAny(), "ReasonCode");
	}

	/**
	 * Create the port to the XKMS service.
	 */
	private XKMSPortType getXKMSPort() {
		// Create the port
		XKMSPortType xkmsPort = getXKMSServiceInstance().getXKMSPort();

		// Set its endpoint address
		BindingProvider bindingProvider = (BindingProvider) xkmsPort;
		bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpointAddress);

		// Add the handlers
		Binding binding = bindingProvider.getBinding();
		@SuppressWarnings("rawtypes")
		List<Handler> handlerChain = binding.getHandlerChain();
		handlerChain.add(new XKMSSignatureSOAPHandler(parameters));
		handlerChain.add(new LoggingSoapHandler());
		if (extraHandlers != null) {
			for (SOAPHandler<SOAPMessageContext> handler : extraHandlers) {
				handlerChain.add(handler);
			}
		}

		binding.setHandlerChain(handlerChain);

		return xkmsPort;
	}

	/**
	 * Creates an instance of the XKMS service.
	 */
	private XKMSService getXKMSServiceInstance() {
		URL wsdlLocation = XKMSClient.class.getResource(WSDL_RESOURCE);
		if (null == wsdlLocation) {
			throw new RuntimeException("WSDL location not valid: " + WSDL_RESOURCE);
		}

		return new XKMSService(wsdlLocation, new QName(NAMESPACE_URI, SERVICE_NAME));
	}

	/**
	 * Parses the certificate result element and extracts the certificate data.
	 */
	private byte[] parseCreateCertificateResult(RegisterResult result) throws XKMSClientException {
		KeyBindingType keyBinding = getKeyBinding(result);

		// Check the result
		if (result.getResult() != ResultCode.SUCCESS) {
			throwXKMSClientExceptionWithReasonCode(keyBinding);
		}

		// Extract the X509 data
		List<Object> content = keyBinding.getKeyInfo().getContent();
		X509DataType x509Data = getFromList(X509DataType.class, content);
		if (x509Data == null) {
			throw new XKMSClientException("No X509Data found in the reply from XKMS.");
		}

		byte[] certificateData = getFromList(byte[].class, x509Data.getX509IssuerSerialOrX509SKIOrX509SubjectName());
		if (certificateData == null) {
			throw new XKMSClientException("No certificate found in the reply from XKMS.");
		}

		return certificateData;
	}

	/**
	 * Parses the certificate revocation element and extracts the certificate
	 * data.
	 */
	private void parseRevokeCertificateResult(RegisterResult result) throws XKMSClientException {
		// Parse the result
		KeyBindingType keyBinding = getKeyBinding(result);
		if (result.getResult() == ResultCode.SUCCESS && keyBinding.getStatus() == AssertionStatus.INVALID) {
			return;
		}

		BigInteger reasonCode = getResultReasonCode(keyBinding);
		if (reasonCode != null && reasonCode.intValue() == 566) {
			// already revoked
			return;
		}

		throwXKMSClientExceptionWithReasonCode(keyBinding);
	}

	/**
	 * Throw an exception with reason code and reason.
	 */
	private void throwXKMSClientExceptionWithReasonCode(KeyBindingType keyBinding) throws XKMSClientException {
		throw new XKMSClientException("Error during revocation: reasonCode=" + getResultReasonCode(keyBinding)
				+ ", reason=" + getResultReason(keyBinding));
	}
}
