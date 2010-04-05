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
import java.security.cert.X509Certificate;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.w3._2000._09.xmldsig_.KeyInfoType;
import org.w3._2002._03.xkms_xbulk.BatchHeaderType;
import org.w3._2002._03.xkms_xbulk.ObjectFactory;
import org.w3._2002._03.xkms_xbulk.RequestType;
import org.w3._2002._03.xkms_xbulk.RequestsType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType.SignedPart;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSService;
import org.xkms.schema.xkms_2001_01_20.Respond;

public class XKMSClient {

	private final String endpointAddress;

	public XKMSClient() {
		this("http://localhost:8080/eid-pki-ra-ws/xkms");
	}

	public XKMSClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	public X509Certificate createCertificate(byte[] csrData) {
		XKMSService xkmsService = XKMSServiceFactory.getInstance();
		XKMSPortType xkmsPort = xkmsService.getXKMSPort();

		BindingProvider bindingProvider = (BindingProvider) xkmsPort;
		bindingProvider.getRequestContext()
				.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						this.endpointAddress);

		Binding binding = bindingProvider.getBinding();
		List<Handler> handlerChain = binding.getHandlerChain();
		handlerChain.add(new XKMSSignatureSOAPHandler());
		handlerChain.add(new LoggingSoapHandler());
		binding.setHandlerChain(handlerChain);

		ObjectFactory xbulkObjectFactory = new ObjectFactory();
		org.xkms.schema.xkms_2001_01_20.ObjectFactory xkmsObjectFactory = new org.xkms.schema.xkms_2001_01_20.ObjectFactory();
		org.w3._2000._09.xmldsig_.ObjectFactory xmldsigObjectFactory = new org.w3._2000._09.xmldsig_.ObjectFactory();
		DatatypeFactory datatypeFactory;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}

		SignedPart signedPart = xbulkObjectFactory
				.createBulkRegisterTypeSignedPart();
		String signedPartId = "signed-part-" + UUID.randomUUID().toString();
		signedPart.setId(signedPartId);

		BatchHeaderType batchHeader = xbulkObjectFactory
				.createBatchHeaderType();
		String batchId = "batch-id-" + UUID.randomUUID().toString();
		batchHeader.getBatchIDAndBatchTimeAndNumberOfRequests().add(batchId);
		GregorianCalendar batchTime = new GregorianCalendar();
		batchTime.setTimeZone(TimeZone.getTimeZone("Z"));
		batchHeader.getBatchIDAndBatchTimeAndNumberOfRequests().add(
				datatypeFactory.newXMLGregorianCalendar(batchTime));
		batchHeader.getBatchIDAndBatchTimeAndNumberOfRequests().add(
				BigInteger.valueOf(1));
		signedPart.setBatchHeader(batchHeader);

		Respond respond = xkmsObjectFactory.createRespond();
		respond.getString().add("RetrievalMethod");
		respond.getString().add("X509Cert");
		signedPart.setRespond(respond);

		RequestsType requests = xbulkObjectFactory.createRequestsType();
		signedPart.setRequests(requests);
		requests.setNumber("1");
		List<RequestType> requestList = requests.getRequest();
		RequestType request = xbulkObjectFactory.createRequestType();
		requestList.add(request);
		String keyId = "key-id-" + UUID.randomUUID().toString();
		request.setKeyID(keyId);
		KeyInfoType keyInfo = xmldsigObjectFactory.createKeyInfoType();
		JAXBElement<byte[]> pkcs10Element = xbulkObjectFactory
				.createPKCS10(csrData);
		keyInfo.getContent().add(pkcs10Element);
		request.setKeyInfo(keyInfo);

		xkmsPort.bulkRegister(signedPart, null, null);

		return null;
	}
}
