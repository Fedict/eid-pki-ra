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
package be.fedict.eid.pkira.blm.model.contracthandler;

import static be.fedict.eid.pkira.blm.model.contracthandler.util.ResponseTypeMatcher.responseType;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jboss.seam.log.Logging;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.domain.AbstractContract;
import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.domain.DomainRepository;
import be.fedict.eid.pkira.blm.model.eiddss.SignatureVerifier;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;
import be.fedict.eid.pkira.blm.model.validation.FieldValidator;
import be.fedict.eid.pkira.blm.model.xkms.XKMSService;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningResponseBuilder;
import be.fedict.eid.pkira.contracts.EntityBuilder;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.generated.contracts.EntityType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Test for the Contract Parser.
 * 
 * @author Jan Van den Bergh
 */
public class ContractHandlerBeanTest {

	private static final CertificateTypeType VALID_CERTIFICATETYPE = CertificateTypeType.CLIENT;
	private static final String VALID_CSR = "csr";
	private static final String VALID_DESCRIPTION = "description";
	private static final String VALID_DN = "dn";
	private static final String VALID_FUNCTION = "function";
	private static final String VALID_LEGALNOTICE = "legalNotice";
	private static final String VALID_NAME = "name";
	private static final String VALID_PHONE = "phone";
	private static final String VALID_REQUEST_ID = "TEST-REQUEST-1";
	private static final int VALID_VALIDITYPERIOD = 15;

	private static final String VALID_ISSUER = "issuer";
	private static final Date VALID_START = new GregorianCalendar(2010, 0, 1).getTime();
	private static final Date VALID_END = new GregorianCalendar(2011, 0, 1).getTime();
	private static final BigInteger VALID_SERIALNUMBER = BigInteger.TEN;

	private static final EntityType VALID_OPERATOR = createValidOperator();
	private static final CertificateSigningRequestType VALID_REQUEST = createValidSigningRequest();
	private static final String VALID_CERTIFICATE = "Certificate";
	private static final CertificateInfo VALID_CERTIFICATE_INFO = createValidCertificateInfo();
	
	private static final String REQUEST_MESSAGE = "Request message";
	private static final String RESPONSE_MESSAGE = "Response message";
	private static final String ERROR_MSG = "Error";

	private static final String SIGNER = "69123110110";	

	private ContractHandlerBean bean;

	@Mock
	private ContractParser contractParser;
	@Mock
	private FieldValidator fieldValidator;
	@Mock
	private SignatureVerifier signatureVerifier;
	@Mock
	private XKMSService xkmsService;
	@Mock
	private CertificateParser certificateParser;
	@Mock
	private DomainRepository domainRepository;
	@Mock
	private MailTemplate mailTemplate;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new ContractHandlerBean();
		bean.setContractParser(contractParser);
		bean.setFieldValidator(fieldValidator);
		bean.setSignatureVerifier(signatureVerifier);
		bean.setDomainRepository(domainRepository);
		bean.setXkmsService(xkmsService);
		bean.setCertificateParser(certificateParser);
		bean.setMailTemplate(mailTemplate);
		
		bean.setLog(Logging.getLog(ContractHandlerBean.class));		
	}

	@Test
	public void testFillResponseFromRequest() {
		CertificateSigningResponseBuilder responseBuilder = new CertificateSigningResponseBuilder();
		bean.fillResponseFromRequest(responseBuilder, createValidSigningRequest(), ResultType.BACKEND_ERROR,
				RESPONSE_MESSAGE);
		CertificateSigningResponseType response = responseBuilder.toResponseType();

		assertEquals(response.getRequestId(), VALID_REQUEST_ID);
		assertEquals(response.getResult(), ResultType.BACKEND_ERROR);
		assertEquals(response.getResultMessage(), RESPONSE_MESSAGE);
		assertNotNull(response.getResponseId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSignCertificateHappyFlow() throws Exception {
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(VALID_REQUEST);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE))).thenReturn(SIGNER);
		when(xkmsService.sign(VALID_CSR)).thenReturn(VALID_CERTIFICATE);
		when(certificateParser.parseCertificate(VALID_CERTIFICATE)).thenReturn(VALID_CERTIFICATE_INFO);
		when(contractParser.marshalResponseMessage(
			argThat(responseType(CertificateSigningResponseType.class, VALID_REQUEST_ID, ResultType.SUCCESS)), 
			eq(CertificateSigningResponseType.class)))
			.thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it
		assertEquals(result, RESPONSE_MESSAGE);
		verify(domainRepository).persistContract(isA(CertificateSigningContract.class));
		verify(domainRepository).persistCertificate(isA(Certificate.class));
		verify(mailTemplate).sendTemplatedMail(anyString(), anyMap(), any(String[].class), any(byte[].class), anyString(), anyString());
	}

	@Test
	public void testSignCertificateInvalidSignature() throws Exception {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
			.thenReturn(VALID_REQUEST);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE)))
			.thenThrow(new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, ERROR_MSG));
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateSigningResponseType.class, VALID_REQUEST_ID, ResultType.INVALID_SIGNATURE)), 
				eq(CertificateSigningResponseType.class)))
			.thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it		
		verifyNoMoreInteractions(domainRepository, xkmsService);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateUnmarshalError() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
			.thenThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, ERROR_MSG));
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateSigningResponseType.class, null, ResultType.INVALID_MESSAGE)), 
				eq(CertificateSigningResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);
	
		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);
	
		// Validate it		
		verifyNoMoreInteractions(domainRepository, xkmsService);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateValidationFailure() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
			.thenReturn(VALID_REQUEST);
		doThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, ERROR_MSG))
			.when(fieldValidator).validateContract(VALID_REQUEST);
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateSigningResponseType.class, VALID_REQUEST_ID, ResultType.INVALID_MESSAGE)), 
				eq(CertificateSigningResponseType.class)))
			.thenReturn(RESPONSE_MESSAGE);		

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it		
		verifyNoMoreInteractions(domainRepository, xkmsService);
		assertEquals(result, RESPONSE_MESSAGE);
	}
	
	@Test
	public void testSignCertificateXKMSFailure() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
			.thenReturn(VALID_REQUEST);
		when(xkmsService.sign(VALID_CSR)).thenReturn(null);
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateSigningResponseType.class, VALID_REQUEST_ID, ResultType.BACKEND_ERROR)), 
				eq(CertificateSigningResponseType.class)))
			.thenReturn(RESPONSE_MESSAGE);		

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it		
		verify(domainRepository).persistContract(any(AbstractContract.class));
		verifyNoMoreInteractions(domainRepository);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	/**
	 * @return
	 */
	private static EntityType createValidOperator() {
		return new EntityBuilder().setFunction(VALID_FUNCTION).setName(VALID_NAME).setPhone(VALID_PHONE).toEntityType();
	}

	private static CertificateSigningRequestType createValidSigningRequest() {
		return new CertificateSigningRequestBuilder(VALID_REQUEST_ID).setCertificateType(VALID_CERTIFICATETYPE).setCsr(
				VALID_CSR).setDescription(VALID_DESCRIPTION).setDistinguishedName(VALID_DN).setLegalNotice(
				VALID_LEGALNOTICE).setOperator(VALID_OPERATOR).setValidityPeriodMonths(VALID_VALIDITYPERIOD)
				.toRequestType();
	}

	private static CertificateInfo createValidCertificateInfo() {
		return new CertificateInfo(VALID_ISSUER, VALID_DN, VALID_START, VALID_END, VALID_SERIALNUMBER);
	}

}
