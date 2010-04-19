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

package be.fedict.eid.pki.ra.xkms.ws;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.JAXBElement;

import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.jboss.seam.log.Logging;
import org.w3._2000._09.xmldsig_.KeyInfoType;
import org.w3._2000._09.xmldsig_.X509DataType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterResultType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterType;
import org.w3._2002._03.xkms_xbulk.RegisterResultsType;
import org.w3._2002._03.xkms_xbulk.RequestType;
import org.w3._2002._03.xkms_xbulk.BulkRegisterResultType.SignedPart;
import org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType;
import org.xkms.schema.xkms_2001_01_20.AssertionStatus;
import org.xkms.schema.xkms_2001_01_20.KeyBindingType;
import org.xkms.schema.xkms_2001_01_20.RegisterResult;
import org.xkms.schema.xkms_2001_01_20.ResultCode;
import org.xkms.schema.xkms_2001_01_20.KeyBindingType.ProcessInfo;
import org.xkms.schema.xkms_2001_01_20.RegisterResult.Answer;

import be.fedict.eid.pkira.crypto.CSRParserImpl;

@WebService(endpointInterface = "org.w3._2002._03.xkms_xbulk_wsdl.XKMSPortType")
public class MockXKMSWebService implements XKMSPortType {

	private final org.w3._2002._03.xkms_xbulk.ObjectFactory xbulkObjectFactory = new org.w3._2002._03.xkms_xbulk.ObjectFactory();
	private final org.xkms.schema.xkms_2001_01_20.ObjectFactory xkmsObjectFactory = new org.xkms.schema.xkms_2001_01_20.ObjectFactory();
	private final org.w3._2000._09.xmldsig_.ObjectFactory dsigObjectFactory = new org.w3._2000._09.xmldsig_.ObjectFactory();
	private final com.ubizen.og.xkms.schema.xkms_2003_09.ObjectFactory ogcmObjectFactory = new com.ubizen.og.xkms.schema.xkms_2003_09.ObjectFactory(); 
	
	@Override
	public BulkRegisterResultType bulkRegister(BulkRegisterType bulkRegister) {	
		RequestType request = bulkRegister.getSignedPart().getRequests().getRequest().get(0);
		
		// See if revoke or request
		RegisterResult registerResult;
		if (request.getStatus()==AssertionStatus.INVALID) {
			// Revocation
			registerResult = processRevocation(request);
		} else {
			// Request
			registerResult = processRequest(request);
		}
		
		// Create the result
		BulkRegisterResultType result = xbulkObjectFactory.createBulkRegisterResultType();
		
		SignedPart signedPart = xbulkObjectFactory.createBulkRegisterResultTypeSignedPart();
		signedPart.setBatchHeader(bulkRegister.getSignedPart().getBatchHeader());
		result.setSignedPart(signedPart);		
				
		RegisterResultsType registerResults = xbulkObjectFactory.createRegisterResultsType();
		signedPart.setRegisterResults(registerResults);
		registerResults.setNumber("1");
		registerResults.getRegisterResult().add(registerResult);
		
		return result;		
	}
	
	private RegisterResult processRevocation(RequestType request) {
		RegisterResult registerResult = createRegisterResultWithKeyBinding();
		
		String keyName = getFromList(String.class, request.getKeyInfo().getContent());		
		KeyBindingType keyBinding = registerResult.getAnswer().getKeyBinding().get(0);
		if (keyName.contains("1002")) {
			registerResult.setResult(ResultCode.FAILURE);
			addReasonAndReasonCode(keyBinding, BigInteger.valueOf(123), "Error");
		} else if (keyName.contains("1001")) {
			registerResult.setResult(ResultCode.FAILURE);
			addReasonAndReasonCode(keyBinding, BigInteger.valueOf(566), "Already revoked");
		} else {
			keyBinding.setStatus(AssertionStatus.INVALID);
			addReasonAndReasonCode(keyBinding, BigInteger.ZERO, "No reason");
		}
		
		return registerResult;		
	}

	private void addReasonAndReasonCode(KeyBindingType keyBinding, BigInteger reasonCode, String reason) {
		ProcessInfo processInfo = xkmsObjectFactory.createKeyBindingTypeProcessInfo();
		keyBinding.setProcessInfo(processInfo);
		
		processInfo.getAny().add(ogcmObjectFactory.createReason(reason));		
		processInfo.getAny().add(ogcmObjectFactory.createReasonCode(reasonCode));
	}

	/**
	 * @param request
	 * @return
	 */
	private RegisterResult processRequest(RequestType request) {
		KeyInfoType requestKeyInfo = request.getKeyInfo();
		byte[] csr = getFromList(byte[].class, requestKeyInfo.getContent());
		byte[] certificate = createCertificate(csr);
		
		// Build result message
		RegisterResult registerResult = createRegisterResultWithKeyBinding();		
		KeyBindingType keyBinding = registerResult.getAnswer().getKeyBinding().get(0);
		
		KeyInfoType keyInfo = dsigObjectFactory.createKeyInfoType();
		keyBinding.setKeyInfo(keyInfo);
		
		X509DataType x509Data = dsigObjectFactory.createX509DataType();		
		x509Data.getX509IssuerSerialOrX509SKIOrX509SubjectName().add(
				dsigObjectFactory.createX509DataTypeX509Certificate(certificate));
		keyInfo.getContent().add(dsigObjectFactory.createX509Data(x509Data));
		
		addReasonAndReasonCode(keyBinding, BigInteger.ZERO, "No reason");
		
		return registerResult;
	}

	private RegisterResult createRegisterResultWithKeyBinding() {
		RegisterResult registerResult = xkmsObjectFactory.createRegisterResult();
		registerResult.setResult(ResultCode.SUCCESS);
						
		Answer answer = xkmsObjectFactory.createRegisterResultAnswer();
		registerResult.setAnswer(answer);
		
		KeyBindingType keyBinding = xkmsObjectFactory.createKeyBindingType();		
		keyBinding.setStatus(AssertionStatus.VALID);
		keyBinding.setId("123");
		answer.getKeyBinding().add(keyBinding);
		return registerResult;
	}

	private byte[] createCertificate(byte[] csr) {
		try {
			CSRParserImpl csrParserImpl = createCSRParser();
			String dn = csrParserImpl.parseCSR(csr).getSubject();

			Date startDate = new Date();
			Date expiryDate = new Date(startDate.getTime() + 1000L * 3600 * 24 * 360);
			BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
			generator.initialize(512);
			KeyPair keyPair = generator.generateKeyPair();

			X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
			X500Principal dnName = new X500Principal(dn);

			certGen.setSerialNumber(serialNumber);
			certGen.setIssuerDN(dnName);
			certGen.setNotBefore(startDate);
			certGen.setNotAfter(expiryDate);
			certGen.setSubjectDN(dnName);
			certGen.setPublicKey(keyPair.getPublic());
			certGen.setSignatureAlgorithm("SHA1withRSA");

			X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC");
			return cert.getEncoded();
		} catch (Exception e) {
			throw new RuntimeException("Error creating self-signed demo certificate", e);
		}
	}

	private CSRParserImpl createCSRParser() {
		CSRParserImpl csrParserImpl = new CSRParserImpl();
		csrParserImpl.setLog(Logging.getLog(MockXKMSWebService.class) );
		return csrParserImpl;
	}

	/**
	 * Extracts the first element from the list matching the type.
	 * @param <T> expected type.
	 * @param clazz expected type.
	 * @param list list with either this type of a JAXBElement with this type.
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

}
