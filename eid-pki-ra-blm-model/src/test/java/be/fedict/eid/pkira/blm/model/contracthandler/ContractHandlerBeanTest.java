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

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.contracthandler.services.ContractParser;
import be.fedict.eid.pkira.blm.model.contracthandler.services.FieldValidator;
import be.fedict.eid.pkira.blm.model.contracthandler.services.SignatureVerifier;
import be.fedict.eid.pkira.blm.model.contracthandler.services.XKMSService;
import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.contracts.ContractRepository;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationManager;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.contracts.CertificateRevocationRequestBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningRequestBuilder;
import be.fedict.eid.pkira.contracts.CertificateSigningResponseBuilder;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Test for the Contract Parser.
 * 
 * @author Jan Van den Bergh
 */
public class ContractHandlerBeanTest {

	private static final String ERROR_MSG = "Error";
	private static final String REQUEST_MESSAGE = "Request message";
	private static final String RESPONSE_MESSAGE = "Response message";
	private static final String SIGNER = "69123110110";
	private static final String REQUESTER = "69123110110";	
	private static final String VALID_CERTIFICATE = "Certificate";
	private static final CertificateTypeType VALID_CERTIFICATETYPETYPE = CertificateTypeType.CLIENT;
	private static final CertificateType VALID_CERTIFICATETYPE = CertificateType.CLIENT;
	private static final String VALID_CSR = "csr";
	private static final String VALID_DN = "dn";
	private static final Date VALID_END = new GregorianCalendar(2011, 0, 1).getTime();
	private static final String VALID_ISSUER = "issuer";
	private static final String VALID_REQUEST_ID = "TEST-REQUEST-1";
	private static final BigInteger VALID_SERIALNUMBER = BigInteger.ONE;
	private static final Date VALID_START = new GregorianCalendar(2010, 0, 1).getTime();
	private static final int VALID_VALIDITYPERIOD = 15;
	
	private static final Certificate THE_CERTIFICATE = createValidCertificate();
	
	private static final Registration VALID_REGISTRATION = createValidRegistration();
	private static final CertificateRevocationRequestType VALID_REVOCATION_REQUEST = createMinimalRevocationRequest();
	private static final CertificateSigningRequestType VALID_SIGNING_REQUEST = createMinimalSigningRequest();
	
	private ContractHandlerBean bean;

	@Mock
	private CertificateParser certificateParser;
	@Mock
	private ContractParser contractParser;
	@Mock
	private ContractRepository contractRepository;
	@Mock
	private FieldValidator fieldValidator;
	@Mock
	private MailTemplate mailTemplate;
	@Mock
	private RegistrationManager registrationManager;
	@Mock
	private SignatureVerifier signatureVerifier;
	@Mock
	private XKMSService xkmsService;
	@Mock
	private CertificateInfo certificateInfo;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);

		bean = new ContractHandlerBean() {
			protected void scheduleNotificationMail(Registration registration,
			CertificateInfo certificateInfo, Certificate certificate) {
				
			}
		};
		bean.setContractParser(contractParser);
		bean.setFieldValidator(fieldValidator);
		bean.setSignatureVerifier(signatureVerifier);
		bean.setContractRepository(contractRepository);
		bean.setXkmsService(xkmsService);
		bean.setCertificateParser(certificateParser);
		bean.setMailTemplate(mailTemplate);
		bean.setRegistrationManager(registrationManager);

		bean.setLog(Logging.getLog(ContractHandlerBean.class));
	}

	@Test
	public void testFillResponseFromRequest() {
		CertificateSigningResponseBuilder responseBuilder = new CertificateSigningResponseBuilder();
		bean.fillResponseFromRequest(responseBuilder, VALID_SIGNING_REQUEST, 
				ResultType.BACKEND_ERROR, RESPONSE_MESSAGE);
		CertificateSigningResponseType response = responseBuilder.toResponseType();

		assertEquals(response.getRequestId(), VALID_REQUEST_ID);
		assertEquals(response.getResult(), ResultType.BACKEND_ERROR);
		assertEquals(response.getResultMessage(), RESPONSE_MESSAGE);
		assertNotNull(response.getResponseId());
	}

	@Test
	public void testRevokeCertificateHappyFlow() throws Exception {
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateRevocationRequestType.class)))
				.thenReturn(VALID_REVOCATION_REQUEST);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE))).thenReturn(SIGNER);
		when(certificateParser.parseCertificate(VALID_CERTIFICATE)).thenReturn(certificateInfo);
		when(certificateInfo.getIssuer()).thenReturn(VALID_ISSUER);
		when(certificateInfo.getSerialNumber()).thenReturn(VALID_SERIALNUMBER);
		when(contractRepository.findCertificate(VALID_ISSUER, VALID_SERIALNUMBER)).thenReturn(THE_CERTIFICATE);
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateRevocationResponseType.class,
						VALID_REQUEST_ID, 
						ResultType.SUCCESS)), 
				eq(CertificateRevocationResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);
		when(registrationManager.findRegistrationForUserDNAndCertificateType(SIGNER, VALID_DN, VALID_CERTIFICATETYPE)).thenReturn(VALID_REGISTRATION);

		// Run it
		String result = bean.revokeCertificate(REQUEST_MESSAGE);

		// Validate it
		assertEquals(result, RESPONSE_MESSAGE);
		verify(xkmsService).revoke(VALID_CERTIFICATE);
		verify(contractRepository).findCertificate(VALID_ISSUER, VALID_SERIALNUMBER);
		verify(contractRepository).persistContract(isA(CertificateRevocationContract.class));
		verify(contractRepository).removeCertificate(THE_CERTIFICATE);
		verifyNoMoreInteractions(contractRepository);
	}
	
	@Test
	public void testRevokeCertificateSignatureError() throws Exception {
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateRevocationRequestType.class)))
				.thenReturn(VALID_REVOCATION_REQUEST);
		when(signatureVerifier.verifySignature(REQUEST_MESSAGE)).thenThrow(new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, ERROR_MSG));
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateRevocationResponseType.class, VALID_REQUEST_ID,	ResultType.INVALID_SIGNATURE)), 
				eq(CertificateRevocationResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = bean.revokeCertificate(REQUEST_MESSAGE);

		// Validate it
		assertEquals(result, RESPONSE_MESSAGE);
		verifyNoMoreInteractions(xkmsService, contractRepository);
	}
	
	@Test
	public void testRevokeCertificateUnmarshalError() throws Exception {
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateRevocationRequestType.class)))
				.thenThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, ERROR_MSG));
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateRevocationResponseType.class, null,	ResultType.INVALID_MESSAGE)), 
				eq(CertificateRevocationResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = bean.revokeCertificate(REQUEST_MESSAGE);

		// Validate it
		assertEquals(result, RESPONSE_MESSAGE);
		verifyNoMoreInteractions(xkmsService, contractRepository);
	}
	
	@Test
	public void testRevokeCertificateValidationFailure() throws Exception {
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateRevocationRequestType.class)))
				.thenReturn(VALID_REVOCATION_REQUEST);
		doThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, ERROR_MSG)).when(fieldValidator).validateContract(VALID_REVOCATION_REQUEST);
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateRevocationResponseType.class, VALID_REQUEST_ID,	ResultType.INVALID_MESSAGE)), 
				eq(CertificateRevocationResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = bean.revokeCertificate(REQUEST_MESSAGE);

		// Validate it
		assertEquals(result, RESPONSE_MESSAGE);
		verifyNoMoreInteractions(xkmsService, contractRepository);
	}
	
	@Test
	public void testRevokeCertificateXKMSFailure() throws Exception {
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateRevocationRequestType.class)))
				.thenReturn(VALID_REVOCATION_REQUEST);
		when(contractRepository.findCertificate(eq(VALID_ISSUER), eq(VALID_SERIALNUMBER))).thenReturn(THE_CERTIFICATE);
		when(certificateParser.parseCertificate(VALID_CERTIFICATE)).thenReturn(certificateInfo);
		when(certificateInfo.getIssuer()).thenReturn(VALID_ISSUER);
		when(certificateInfo.getSerialNumber()).thenReturn(VALID_SERIALNUMBER);		
		doThrow(new ContractHandlerBeanException(ResultType.BACKEND_ERROR, ERROR_MSG)).when(xkmsService).revoke(VALID_CERTIFICATE);
		when(contractParser.marshalResponseMessage(
				argThat(responseType(CertificateRevocationResponseType.class, VALID_REQUEST_ID, ResultType.BACKEND_ERROR)), 
				eq(CertificateRevocationResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);
		when(registrationManager.findRegistrationForUserDNAndCertificateType(SIGNER, VALID_DN, VALID_CERTIFICATETYPE)).thenReturn(VALID_REGISTRATION);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE))).thenReturn(SIGNER);

		// Run it
		String result = bean.revokeCertificate(REQUEST_MESSAGE);

		// Validate it
		assertEquals(result, RESPONSE_MESSAGE);
		verify(contractRepository).findCertificate(VALID_ISSUER, VALID_SERIALNUMBER);
		verify(contractRepository).persistContract(isA(CertificateRevocationContract.class));
		verifyNoMoreInteractions(contractRepository);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSignCertificateHappyFlow() throws Exception {
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(VALID_SIGNING_REQUEST);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE))).thenReturn(SIGNER);
		when(xkmsService.sign(VALID_CSR)).thenReturn(VALID_CERTIFICATE);
		when(certificateParser.parseCertificate(VALID_CERTIFICATE)).thenReturn(certificateInfo);
		when(
				contractParser.marshalResponseMessage(argThat(responseType(CertificateSigningResponseType.class,
						VALID_REQUEST_ID, ResultType.SUCCESS)), eq(CertificateSigningResponseType.class))).thenReturn(
				RESPONSE_MESSAGE);
		when(registrationManager.findRegistrationForUserDNAndCertificateType(SIGNER, VALID_DN, VALID_CERTIFICATETYPE)).thenReturn(VALID_REGISTRATION);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE))).thenReturn(SIGNER);
		when(certificateInfo.getValidityEnd()).thenReturn(new Date());
		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it
		assertEquals(result, RESPONSE_MESSAGE);
		verify(contractRepository).persistContract(isA(CertificateSigningContract.class));
		verify(contractRepository).persistCertificate(isA(Certificate.class));
		verify(mailTemplate).sendTemplatedMail(anyString(), anyMap(), any(String[].class), any(byte[].class),
				anyString(), anyString());
	}

	@Test
	public void testSignCertificateInvalidSignature() throws Exception {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(VALID_SIGNING_REQUEST);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE))).thenThrow(
				new ContractHandlerBeanException(ResultType.INVALID_SIGNATURE, ERROR_MSG));
		when(
				contractParser.marshalResponseMessage(argThat(responseType(CertificateSigningResponseType.class,
						VALID_REQUEST_ID, ResultType.INVALID_SIGNATURE)), eq(CertificateSigningResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verifyNoMoreInteractions(contractRepository, xkmsService);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateUnmarshalError() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, ERROR_MSG));
		when(
				contractParser.marshalResponseMessage(argThat(responseType(CertificateSigningResponseType.class, null,
						ResultType.INVALID_MESSAGE)), eq(CertificateSigningResponseType.class))).thenReturn(
				RESPONSE_MESSAGE);

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verifyNoMoreInteractions(contractRepository, xkmsService);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateValidationFailure() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(VALID_SIGNING_REQUEST);
		doThrow(new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, ERROR_MSG)).when(fieldValidator)
				.validateContract(VALID_SIGNING_REQUEST);
		when(
				contractParser.marshalResponseMessage(argThat(responseType(CertificateSigningResponseType.class,
						VALID_REQUEST_ID, ResultType.INVALID_MESSAGE)), eq(CertificateSigningResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verifyNoMoreInteractions(contractRepository, xkmsService);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	@Test
	public void testSignCertificateXKMSFailure() throws ContractHandlerBeanException {
		// Setup test
		when(contractParser.unmarshalRequestMessage(eq(REQUEST_MESSAGE), eq(CertificateSigningRequestType.class)))
				.thenReturn(VALID_SIGNING_REQUEST);
		when(xkmsService.sign(VALID_CSR)).thenReturn(null);
		when(
				contractParser.marshalResponseMessage(argThat(responseType(CertificateSigningResponseType.class,
						VALID_REQUEST_ID, ResultType.BACKEND_ERROR)), eq(CertificateSigningResponseType.class)))
				.thenReturn(RESPONSE_MESSAGE);
		when(registrationManager.findRegistrationForUserDNAndCertificateType(SIGNER, VALID_DN, VALID_CERTIFICATETYPE)).thenReturn(VALID_REGISTRATION);
		when(signatureVerifier.verifySignature(eq(REQUEST_MESSAGE))).thenReturn(SIGNER);

		// Run it
		String result = bean.signCertificate(REQUEST_MESSAGE);

		// Validate it
		verify(contractRepository).persistContract(any(AbstractContract.class));
		verifyNoMoreInteractions(contractRepository);
		assertEquals(result, RESPONSE_MESSAGE);
	}

	private static CertificateRevocationRequestType createMinimalRevocationRequest() {
		return new CertificateRevocationRequestBuilder(VALID_REQUEST_ID)
			.setDistinguishedName(VALID_DN)
			.setCertificate(VALID_CERTIFICATE)
			.setValidityStart(VALID_START)
			.setValidityEnd(VALID_END)
			.toRequestType();
	}

	private static CertificateSigningRequestType createMinimalSigningRequest() {
		return new CertificateSigningRequestBuilder(VALID_REQUEST_ID)
			.setDistinguishedName(VALID_DN)
			.setCertificateType(VALID_CERTIFICATETYPETYPE)
			.setValidityPeriodMonths(VALID_VALIDITYPERIOD)
			.setCsr(VALID_CSR)
			.toRequestType();
	}
	
	private static Certificate createValidCertificate() {
		Certificate result = new Certificate();
		result.setCertificateType(VALID_CERTIFICATETYPE);
		return result;
	}

	private static Registration createValidRegistration() {
		Registration registration = new Registration();
		
		User requester = new User();
		requester.setFirstName("first");
		requester.setLastName("last");
		registration.setRequester(requester);
		registration.setCertificateDomain(new CertificateDomain());
		
		return registration;
	}

}
