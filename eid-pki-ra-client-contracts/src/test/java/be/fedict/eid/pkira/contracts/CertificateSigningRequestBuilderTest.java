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
package be.fedict.eid.pkira.contracts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;

/**
 * @author Jan Van den Bergh
 */
public class CertificateSigningRequestBuilderTest {

	private static final CertificateTypeType CERTIFICATE_TYPE = CertificateTypeType.CLIENT;
	private static final String CSR = "csr";
	private static final String DESCRIPTION = "description";
	private static final String DN = "dn";
	private static final String LEGAL_NOTICE = "legalNotice";
	private static final String OPERATOR_FUNCTION = "function";
	private static final String OPERATOR_NAME = "name";
	private static final String OPERATOR_PHONE = "phone";
	private static final String REQUEST_ID = "requestId";
	private static final int VALIDITY = 15;

	public static CertificateSigningRequestType createFilledCertificateSigningRequest() {
		CertificateSigningRequestBuilder builder = new CertificateSigningRequestBuilder(REQUEST_ID);
		setOtherFields(builder);
		CertificateSigningRequestType request = builder.toRequestType();
		return request;
	}

	@Test
	public void testCertificateSigningBuilderWithId() {
		CertificateSigningRequestType request = createFilledCertificateSigningRequest();
		
		assertEquals(request.getRequestId(), REQUEST_ID);
		validateOtherFields(request);
	}	
	
	@Test
	public void testCertificateSigningBuilderWithoutId() {
		CertificateSigningRequestBuilder builder = new CertificateSigningRequestBuilder();
		setOtherFields(builder);
		CertificateSigningRequestType request = builder.toRequestType();
		
		assertNotNull(request.getRequestId(), REQUEST_ID);
		validateOtherFields(request);
	}
	
	private static void setOtherFields(CertificateSigningRequestBuilder builder) {
		builder.setDescription(DESCRIPTION)
			.setDistinguishedName(DN)
			.setLegalNotice(LEGAL_NOTICE)		
			.setCertificateType(CERTIFICATE_TYPE)
			.setCsr(CSR)
			.setValidityPeriodMonths(VALIDITY)
			.setOperator(new EntityBuilder()
				.setFunction(OPERATOR_FUNCTION)
				.setName(OPERATOR_NAME)
				.setPhone(OPERATOR_PHONE)
				.toEntityType());
	}

	private void validateOtherFields(CertificateSigningRequestType request) {
		assertEquals(request.getDescription(), DESCRIPTION);
		assertEquals(request.getDistinguishedName(), DN);
		assertEquals(request.getLegalNotice(), LEGAL_NOTICE);
		assertEquals(request.getOperator().getFunction(), OPERATOR_FUNCTION);
		assertEquals(request.getOperator().getName(), OPERATOR_NAME);
		assertEquals(request.getOperator().getPhone(), OPERATOR_PHONE);		
		assertEquals(request.getCertificateType(), CERTIFICATE_TYPE);
		assertEquals(request.getCSR(), CSR);	
		assertEquals(request.getValidityPeriodMonths().intValue(), VALIDITY);	
	}
}
