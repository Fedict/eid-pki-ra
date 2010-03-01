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
package be.fedict.eid.blm.model.validation;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.contracts.util.JAXBUtil;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.generated.contracts.EntityType;

public class FieldValidatorBeanTest {

	// TODO test for validation of signing requests
	// TODO test for validation of revocation requests

	private static final CertificateTypeType INVALID_CERTIFICATE_TYPE = null;
	private static final String INVALID_CSR = "";
	private static final String INVALID_DESCRIPTION = "";
	private static final String INVALID_DN = "";
	private static final String INVALID_EMAIL = "j.vandenbergh@aca-it@be";
	private static final String INVALID_FUNCTION = "";
	private static final String INVALID_LEGAL_NOTICE = "";
	private static final String INVALID_NAME = null;
	private static final String INVALID_PHONE = "abcde";
	private static final int INVALID_VALIDITY_PERIOD = 23;
	private static final CertificateTypeType VALID_CERTIFICATE_TYPE = CertificateTypeType.SERVER;
	private static final String VALID_CSR = "-----BEGIN NEW CERTIFICATE REQUEST-----\r\n"
			+ "MIICbzCCAi0CAQAwajELMAkGA1UEBhMCQkUxEDAOBgNVBAgTB0xpbWJ1cmcxEDAOBgNVBAcTB0hh\r\n"
			+ "c3NlbHQxDDAKBgNVBAoTA0FDQTENMAsGA1UECxMEdGVzdDEaMBgGA1UEAxMRSmFuIFZhbiBkZW4g\r\n"
			+ "QmVyZ2gwggG4MIIBLAYHKoZIzjgEATCCAR8CgYEA/X9TgR11EilS30qcLuzk5/YRt1I870QAwx4/\r\n"
			+ "gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuAHTRv8mZgt2uZUKWkn5/oBHsQIsJPu6nX/rfG\r\n"
			+ "G/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOuK2HXKu/yIgMZndFIAccCFQCXYFCPFSMLzLKS\r\n"
			+ "uYKi64QL8Fgc9QKBgQD34aCF1ps93su8q1w2uFe5eZSvu/o66oL5V0wLPQeCZ1FZV4661FlP5nEH\r\n"
			+ "EIGAtEkWcSPoTCgWE7fPCTKMyKbhPBZ6i1R8jSjgo64eK7OmdZFuo38L+iE1YvH7YnoBJDvMpPG+\r\n"
			+ "qFGQiaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOBhQACgYEA+86jKc18tmTaU44RdbeQIkBi5R4q\r\n"
			+ "KGvWiuoIcoKaQswraNkLzlGLlJbsfIGA+aZbqaZkvNKpRU+7OVwW1FBuPCaXuhDL315XvLQ/kz4/\r\n"
			+ "Ft5x70OccrJqzTxecvUyjwTrhehyxURBZ4e+oCrYp9py3zMmy2qDDWIN1IYTdF+VzxSgADALBgcq\r\n"
			+ "hkjOOAQDBQADLwAwLAIUcQtBbLV6WliL6xr6yFg5IMYMjfsCFB4D9BUGyFYRNvHFms7ySKKdg+Md\r\n"
			+ "-----END NEW CERTIFICATE REQUEST-----";
	private static final String VALID_DESCRIPTION = "New certificate";
	private static final String VALID_DN = "C=BE,ST=Limburg,L=Hasselt,O=ACA,OU=test,CN=Jan Van den Bergh";
	private static final String VALID_EMAIL = "j.vandenbergh@aca-it.be";
	private static final String VALID_FUNCTION = "Operator";
	private static final String VALID_LEGAL_NOTICE = "Legal notice";
	private static final String VALID_NAME = "Jan Van den Bergh";
	private static final String VALID_PHONE = "+32 3 123.456";
	private static final String VALID_REQUEST_ID = "REQUEST_ID";
	private static final int VALID_VALIDITY_PERIOD = 15;

	private FieldValidatorBean bean;
	private List<String> messages;

	@BeforeMethod
	public void setup() {
		bean = new FieldValidatorBean();
		messages = new ArrayList<String>();
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
		} catch (ContractHandlerBeanException e) {
			assertMessages(1, e.getMessages());
		}
	}

	@Test
	public void testValidateCertificateSigningRequestNull() throws ContractHandlerBeanException {
		try {
			bean.validateContract((CertificateSigningRequestType) null);
		} catch (ContractHandlerBeanException e) {
			assertMessages(1, e.getMessages());
		}
	}

	@Test
	public void testValidateInvalidCertificateSigningRequest() {
		try {
			bean.validateContract(createInvalidCertificateSigningRequest());
		} catch (ContractHandlerBeanException e) {
			assertMessages(10, e.getMessages());
		}
	}

	@Test
	public void testValidateInvalidOperator() {
		EntityType operator = createInvalidOperator();
		bean.validateOperator(operator, messages);
		assertMessages(4);
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

	private CertificateSigningRequestType createInvalidCertificateSigningRequest() {
		CertificateSigningRequestType request = new CertificateSigningRequestBuilder(VALID_REQUEST_ID)
				.setCertificateType(INVALID_CERTIFICATE_TYPE).setCsr(INVALID_CSR).setDescription(INVALID_DESCRIPTION)
				.setDistinguishedName(INVALID_DN).setLegalNotice(INVALID_LEGAL_NOTICE).setOperator(
						createInvalidOperator()).setValidityPeriodMonths(INVALID_VALIDITY_PERIOD).toRequestType();
		request.setSignature(JAXBUtil.getObjectFactory().createSignatureType());
		return request;
	}

	private EntityType createInvalidOperator() {
		return new EntityBuilder().setEmail(INVALID_EMAIL).setPhone(INVALID_PHONE).setName(INVALID_NAME).setFunction(
				INVALID_FUNCTION).toEntityType();
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
		return new EntityBuilder().setEmail(VALID_EMAIL).setPhone(VALID_PHONE).setName(VALID_NAME).setFunction(
				VALID_FUNCTION).toEntityType();
	}
}
