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
import java.util.List;

import org.w3._2000._09.xmldsig_.X509DataType;
import org.xkms.schema.xkms_2001_01_20.AssertionStatus;
import org.xkms.schema.xkms_2001_01_20.KeyBindingType;
import org.xkms.schema.xkms_2001_01_20.RegisterResult;
import org.xkms.schema.xkms_2001_01_20.RegisterResult.Answer;
import org.xkms.schema.xkms_2001_01_20.ResultCode;

import be.fedict.eid.pkira.xkmsws.XKMSClientException;

public class ResponseMessageParser {

	private final XMLMarshallingUtil marshallingUtil;

	public ResponseMessageParser(XMLMarshallingUtil marshallingUtil) {
		this.marshallingUtil = marshallingUtil;
	}

	/**
	 * Parses the certificate result element and extracts the certificate data.
	 */
	public byte[] parseCreateCertificateResult(RegisterResult result) throws XKMSClientException {
		KeyBindingType keyBinding = getKeyBinding(result);

		// Check the result
		if (result.getResult() != ResultCode.SUCCESS) {
			throwXKMSClientExceptionWithReasonCode(keyBinding);
		}

		// Extract the X509 data
		List<Object> content = keyBinding.getKeyInfo().getContent();
		X509DataType x509Data = marshallingUtil.getFromList(X509DataType.class, content);
		if (x509Data == null) {
			throw new XKMSClientException("No X509Data found in the reply from XKMS.");
		}

		byte[] certificateData = marshallingUtil.getFromList(byte[].class,
				x509Data.getX509IssuerSerialOrX509SKIOrX509SubjectName());
		if (certificateData == null) {
			throw new XKMSClientException("No certificate found in the reply from XKMS.");
		}

		return certificateData;
	}

	/**
	 * Parses the certificate revocation element and extracts the certificate
	 * data.
	 */
	public void parseRevokeCertificateResult(RegisterResult result) throws XKMSClientException {
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

		return (String) marshallingUtil.getFromJAXBElementList(keyBinding.getProcessInfo().getAny(), "Reason");
	}

	/**
	 * Get the result reason code from the OGCM parameters.
	 */
	private BigInteger getResultReasonCode(KeyBindingType keyBinding) {
		if (keyBinding == null || keyBinding.getProcessInfo() == null || keyBinding.getProcessInfo().getAny() == null) {
			return null;
		}

		Object value = marshallingUtil.getFromJAXBElementList(keyBinding.getProcessInfo().getAny(), "ReasonCode");
		if (value instanceof String) {
			return new BigInteger((String) value);
		}
		return (BigInteger) value;
	}

	/**
	 * Throw an exception with reason code and reason.
	 */
	private void throwXKMSClientExceptionWithReasonCode(KeyBindingType keyBinding) throws XKMSClientException {
		throw new XKMSClientException("Error during revocation: reasonCode=" + getResultReasonCode(keyBinding)
				+ ", reason=" + getResultReason(keyBinding));
	}

}
