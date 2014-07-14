/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
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

package be.fedict.eid.pkira.xkmsws.util;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.w3._2000._09.xmldsig_.KeyInfoType;
import org.w3._2002._03.xkms_xbulk.BatchHeaderType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType.SignedPart;
import org.w3._2002._03.xkms_xbulk.ProcessInfoType;
import org.w3._2002._03.xkms_xbulk.RequestType;
import org.w3._2002._03.xkms_xbulk.RequestsType;
import org.xkms.schema.xkms_2001_01_20.AssertionStatus;
import org.xkms.schema.xkms_2001_01_20.Respond;

import com.ubizen.xkms.kitoshi.AttributeCertificate;
import com.ubizen.xkms.kitoshi.ValidityIntervalType;

public class RequestMessageCreator {

	private final org.w3._2002._03.xkms_xbulk.ObjectFactory xbulkObjectFactory = new org.w3._2002._03.xkms_xbulk.ObjectFactory();
	private final org.xkms.schema.xkms_2001_01_20.ObjectFactory xkmsObjectFactory = new org.xkms.schema.xkms_2001_01_20.ObjectFactory();
	private final org.w3._2000._09.xmldsig_.ObjectFactory xmldsigObjectFactory = new org.w3._2000._09.xmldsig_.ObjectFactory();
	private final com.ubizen.xkms.kitoshi.ObjectFactory ogcmObjectFactory = new com.ubizen.xkms.kitoshi.ObjectFactory();
	private final DatatypeFactory datatypeFactory;
	public static final String KEYNAME_REQUEST = "http://xkms.og.ubizen.com/keyname?buc_id={0}";

	private final Map<String, String> parameters;
	// public static final String KEYNAME_REVOKE =
	// "http://xkms.og.ubizen.com/keyname?buc_id={0}&cert_serialnumber={1}";
	public static final String KEYNAME_REVOKE = "http://xkms.og.ubizen.com/keyname?cert_serialnumber={1}";
	private static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone("Z");
	public static final String PARAMETER_BUC = "buc";

	public RequestMessageCreator(Map<String, String> parameters) {
		this.parameters = parameters;

		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a bulk XKMS request for a CSR.
	 * 
	 * @param certificateType
	 */
	public RequestType createCSRRequestElement(byte[] csrData, int validityInMonths, String certificateType, int timeShiftSeconds) {
		// Create the request
		RequestType request = xbulkObjectFactory.createRequestType();
		request.setKeyID("key-id-" + UUID.randomUUID().toString());

		// Add the key info
		KeyInfoType keyInfo = xmldsigObjectFactory.createKeyInfoType();
		request.setKeyInfo(keyInfo);

		// Add the key name
		String keyName = MessageFormat.format(KEYNAME_REQUEST,
				encodeSpecialCharactersinKeyName(getBuc(certificateType)));
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
		calendar.add(Calendar.SECOND, timeShiftSeconds);
		validityInterval.setNotBefore(datatypeFactory.newXMLGregorianCalendar(calendar));
		calendar.add(Calendar.MONTH, validityInMonths);
		validityInterval.setNotAfter(datatypeFactory.newXMLGregorianCalendar(calendar));
		attributeCertificate.setValidityInterval(validityInterval);

		// Add the status
		request.setStatus(AssertionStatus.VALID);

		return request;
	}

	private String getBuc(String certificateType) {
		return parameters.get(PARAMETER_BUC + "." + certificateType.toLowerCase());
	}

	/**
	 * Encodes a parameter in the key name.
	 */
	private String encodeSpecialCharactersinKeyName(String toEncode) {
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
	 * Creates a bulk XKMS request for a revocation.
	 */
	public RequestType createRevocationElement(BigInteger serialNumber, String certificateType) {
		// Create the request
		RequestType request = xbulkObjectFactory.createRequestType();
		request.setKeyID("key-id-" + UUID.randomUUID().toString());
		request.setStatus(AssertionStatus.INVALID);

		// Add the key info
		KeyInfoType keyInfo = xmldsigObjectFactory.createKeyInfoType();
		request.setKeyInfo(keyInfo);

		// Add the key name
		String keyName = MessageFormat.format(KEYNAME_REVOKE,
				encodeSpecialCharactersinKeyName(parameters.get(PARAMETER_BUC + "." + certificateType.toLowerCase())),
				encodeSpecialCharactersinKeyName(serialNumber.toString(16)));
		keyInfo.getContent().add(xmldsigObjectFactory.createKeyName(keyName));

		return request;

	}

	/**
	 * Creates a bulk register request containing the specified request objects.
	 */
	public BulkRegisterType createBulkRegisterRequest(RequestType... requestObjects) {
		// Create the signed part
		SignedPart signedPart = xbulkObjectFactory.createBulkRegisterTypeSignedPart();
		String signedPartId = "signed-part";
		signedPart.setId(signedPartId);

		// Add the respond element to it
		Respond respond = xkmsObjectFactory.createRespond();
		respond.getString().add("RetrievalMethod");
		respond.getString().add("X509Cert");
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

}
