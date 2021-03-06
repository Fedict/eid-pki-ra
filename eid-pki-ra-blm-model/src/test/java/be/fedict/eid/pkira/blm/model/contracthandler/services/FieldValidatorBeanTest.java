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
package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.config.ConfigurationEntry;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.contracts.CertificateRevocationRequestBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.contracts.util.JAXBUtil;
import be.fedict.eid.pkira.crypto.certificate.CertificateInfo;
import be.fedict.eid.pkira.crypto.certificate.CertificateParser;
import be.fedict.eid.pkira.crypto.csr.CSRInfo;
import be.fedict.eid.pkira.crypto.csr.CSRParser;
import be.fedict.eid.pkira.crypto.exception.CryptoException;
import be.fedict.eid.pkira.dnfilter.DistinguishedNameManager;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.generated.contracts.EntityType;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class FieldValidatorBeanTest {

	private static final String INVALID_CERTIFICATE = "Invalid certificate";
	private static final CertificateTypeType INVALID_CERTIFICATE_TYPE = null;
	private static final String INVALID_CSR="Invalid CSR";
	private static final String INVALID_DESCRIPTION = "";
	private static final String INVALID_DN = "";
	private static final String INVALID_FUNCTION = "";
	private static final String INVALID_LEGAL_NOTICE = "";
	private static final String INVALID_NAME = null;
	private static final String INVALID_PHONE = "abcde";
	private static final int INVALID_VALIDITY_PERIOD = 23;
	private static final String VALID_CERTIFICATE = "Valid certificate";
	private static final CertificateTypeType VALID_CERTIFICATE_TYPE = CertificateTypeType.SERVER;
	private static final String VALID_CSR="Valid CSR";
	private static final String VALID_DESCRIPTION = "New certificate";
	private static final String VALID_DN = "C=BE,ST=Limburg,L=Hasselt,O=ACA,OU=test,CN=Jan Van den Bergh";
	private static final Date VALID_ENDDATE = new Date();
	private static final String VALID_FUNCTION = "Operator";
	private static final String VALID_LEGAL_NOTICE = "Legal notice";
	private static final String VALID_NAME = "Jan Van den Bergh";
	private static final String VALID_PHONE = "+32 3 123.456";
	private static final String VALID_REQUEST_ID = "REQUEST_ID";
	private static final Date VALID_STARTDATE = new Date(VALID_ENDDATE.getTime()+1000);
	private static final int VALID_VALIDITY_PERIOD = 15;

	private FieldValidatorBean bean;
	@Mock
	private CertificateParser certificateParser;
	@Mock 
	private CSRParser csrParser;
	@Mock
	private CertificateInfo certificateInfo;
	@Mock
	private CSRInfo csrInfo;
	@Mock
	private ConfigurationEntryQuery configurationEntryQuery;
	@Mock
	private DistinguishedNameManager distinguishedNameManager;
	private List<String> messages;

	@BeforeMethod
	public void setup() throws CryptoException {
		MockitoAnnotations.initMocks(this);
		
		when(csrParser.parseCSR(VALID_CSR)).thenReturn(csrInfo);
		when(csrParser.parseCSR(INVALID_CSR)).thenThrow(new CryptoException());
		when(certificateParser.parseCertificate(VALID_CERTIFICATE)).thenReturn(certificateInfo);
		when(certificateParser.parseCertificate(INVALID_CERTIFICATE)).thenThrow(new CryptoException());
		when(certificateInfo.getValidityStart()).thenReturn(VALID_STARTDATE);
		when(certificateInfo.getValidityEnd()).thenReturn(VALID_ENDDATE);
		when(certificateInfo.getDistinguishedName()).thenReturn(VALID_DN);
		when(csrInfo.getSubject()).thenReturn(VALID_DN);
		
		ConfigurationEntry validities = new ConfigurationEntry();
		validities.setKey(ConfigurationEntryKey.VALIDITY_PERIODS);
		validities.setValue("15,30");
		when(configurationEntryQuery.findByEntryKey(ConfigurationEntryKey.VALIDITY_PERIODS)).thenReturn(validities);
		
		bean = new FieldValidatorBean();		
		bean.setCSRParser(csrParser);
		bean.setCertificateParser(certificateParser);
		bean.setConfigurationEntryQuery(configurationEntryQuery);
		bean.setDistinguishedNameManager(distinguishedNameManager);
		
		messages = new ArrayList<String>();
	}

	@Test
	public void testValidateCertificateRevocationRequest() throws ContractHandlerBeanException {		
		CertificateRevocationRequestType contract = createValidCertificateRevocationRequest();
		bean.validateContract(contract);
	}

	@Test
	public void testValidateCertificateRevocationRequestInvalidDNAndDates() {			
		CertificateRevocationRequestType contract = createValidCertificateRevocationRequest();
		contract.setDistinguishedName(VALID_DN + ",test=test");
		contract.setValidityStart(contract.getValidityEnd());
		contract.setValidityEnd(null);		
		try {
			bean.validateContract(contract);
			fail("Expected an exception");
		} catch (ContractHandlerBeanException e) {
			assertMessages(3, e.getMessages());
		}
	}

	@Test
	public void testValidateCertificateRevocationRequestNull() throws ContractHandlerBeanException {
		try {
			bean.validateContract((CertificateRevocationRequestType) null);
			fail("Expected an exception");
		} catch (ContractHandlerBeanException e) {
			assertMessages(1, e.getMessages());
		}
	}

	@Test
	public void testValidateCertificateSigningRequest() throws ContractHandlerBeanException {		
		CertificateSigningRequestType contract = createValidCertificateSigningRequest();
		bean.validateContract(contract);
	}
	
	@Test
	public void testValidateCertificateSigningRequestInvalidDN() {
		CertificateSigningRequestType contract = createValidCertificateSigningRequest();
		contract.setDistinguishedName(VALID_DN + ",test=test");
		try {
			bean.validateContract(contract);
			fail("Expected an exception");
		} catch (ContractHandlerBeanException e) {
			assertMessages(1, e.getMessages());
		}
	}

	@Test
	public void testValidateCertificateSigningRequestNull() throws ContractHandlerBeanException {
		try {
			bean.validateContract((CertificateSigningRequestType) null);
			fail("Expected an exception");
		} catch (ContractHandlerBeanException e) {
			assertMessages(1, e.getMessages());
		}
	}

	@Test
	public void testValidateInvalidCertificateRevocationRequest() {
		try {
			bean.validateContract(createInvalidCertificateRevocationRequest());
			fail("Expected an exception");
		} catch (ContractHandlerBeanException e) {
			assertMessages(9, e.getMessages());
		}
	}

	@Test
	public void testValidateInvalidCertificateSigningRequest() {
		try {
			bean.validateContract(createInvalidCertificateSigningRequest());
			fail("Expected an exception");
		} catch (ContractHandlerBeanException e) {
			assertMessages(9, e.getMessages());
		}
	}

	@Test
	public void testValidateInvalidOperator() {
		EntityType operator = createInvalidOperator();
		bean.validateOperator(operator, messages);
		assertMessages(3);
	}

	@Test
	public void testValidateNullOperator() {
		bean.validateOperator(null, messages);
		assertMessages(1);
	}

	@Test
	public void testValidateOperator() {
		EntityType operator = createValidOperator();
		bean.validateOperator(operator, messages);
		assertMessages(0);
	}

	private void assertMessages(int count) {
		assertMessages(count, messages);
	}

	private void assertMessages(int count, List<String> messages) {
		System.out.println("Messages: " + messages);
		assertEquals(messages.size(), count, "Expected " + count + " validation errors, but found " + messages.size()
				+ ": " + messages);
	}
	
	private CertificateRevocationRequestType createInvalidCertificateRevocationRequest() {
		CertificateRevocationRequestType request = new CertificateRevocationRequestBuilder(null)
				.setCertificate(INVALID_CERTIFICATE)
				.setDescription(INVALID_DESCRIPTION)
				.setDistinguishedName(INVALID_DN)
				.setValidityEnd(null)
				.setValidityStart(null)
				.setLegalNotice(INVALID_LEGAL_NOTICE)
				.setOperator(createInvalidOperator())
				.toRequestType();
		request.setSignature(JAXBUtil.getObjectFactory().createSignatureType());
		return request;
	}

	private CertificateSigningRequestType createInvalidCertificateSigningRequest() {
		CertificateSigningRequestType request = new CertificateSigningRequestBuilder(VALID_REQUEST_ID)
				.setCertificateType(INVALID_CERTIFICATE_TYPE).setCsr(INVALID_CSR).setDescription(INVALID_DESCRIPTION)
				.setDistinguishedName(INVALID_DN).setLegalNotice(INVALID_LEGAL_NOTICE).setOperator(
						createInvalidOperator()).setValidityPeriodMonths(INVALID_VALIDITY_PERIOD).toRequestType();
		request.setSignature(JAXBUtil.getObjectFactory().createSignatureType());
		return request;
	}

	private EntityType createInvalidOperator() {
		return new EntityBuilder().setPhone(INVALID_PHONE).setName(INVALID_NAME).setFunction(
				INVALID_FUNCTION).toEntityType();
	}

	private CertificateRevocationRequestType createValidCertificateRevocationRequest() {
		CertificateRevocationRequestType request = new CertificateRevocationRequestBuilder(VALID_REQUEST_ID)
				.setCertificate(VALID_CERTIFICATE)
				.setDescription(VALID_DESCRIPTION)
				.setDistinguishedName(VALID_DN)
				.setValidityEnd(VALID_ENDDATE)
				.setValidityStart(VALID_STARTDATE)
				.setLegalNotice(VALID_LEGAL_NOTICE)
				.setOperator(createValidOperator())
				.toRequestType();
		request.setSignature(JAXBUtil.getObjectFactory().createSignatureType());
		return request;
	}
	
	private CertificateSigningRequestType createValidCertificateSigningRequest() {
		CertificateSigningRequestType request = new CertificateSigningRequestBuilder(VALID_REQUEST_ID)
				.setCertificateType(VALID_CERTIFICATE_TYPE).setCsr(VALID_CSR).setDescription(VALID_DESCRIPTION)
				.setDistinguishedName(VALID_DN).setLegalNotice(VALID_LEGAL_NOTICE).setOperator(createValidOperator())
				.setValidityPeriodMonths(VALID_VALIDITY_PERIOD).toRequestType();
		request.setSignature(JAXBUtil.getObjectFactory().createSignatureType());
		return request;
	}

	private EntityType createValidOperator() {
		return new EntityBuilder().setPhone(VALID_PHONE).setName(VALID_NAME).setFunction(
				VALID_FUNCTION).toEntityType();
	}
}
