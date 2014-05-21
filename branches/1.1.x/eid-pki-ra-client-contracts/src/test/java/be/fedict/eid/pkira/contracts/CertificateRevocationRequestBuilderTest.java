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
package be.fedict.eid.pkira.contracts;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;

import static be.fedict.eid.pkira.contracts.util.JAXBUtil.createXmlGregorianCalendar;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Jan Van den Bergh
 */
public class CertificateRevocationRequestBuilderTest {

	private static final String CERTIFICATE = "certificate";
	private static final String REQUEST_ID = "requestId";
	private static final String DESCRIPTION = "description";
	private static final String DN = "dn";
	private static final Date END_DATE = new GregorianCalendar(2010, Calendar.DECEMBER, 31).getTime();
	private static final String LEGAL_NOTICE = "legalNotice";
	private static final String OPERATOR_FUNCTION = "function";
	private static final String OPERATOR_NAME = "name";
	private static final String OPERATOR_PHONE = "phone";
	private static final Date START_DATE = new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime();

	@Test
	public void testCertificateRevocationBuilderWithId() {
		CertificateRevocationRequestBuilder builder = new CertificateRevocationRequestBuilder(REQUEST_ID);
		setOtherFields(builder);
		CertificateRevocationRequestType request = builder.toRequestType();
		
		assertEquals(request.getRequestId(), REQUEST_ID);
		validateOtherFields(request);
	}	
	
	@Test
	public void testCertificateRevocationBuilderWithoutId() {
		CertificateRevocationRequestBuilder builder = new CertificateRevocationRequestBuilder();
		setOtherFields(builder);
		CertificateRevocationRequestType request = builder.toRequestType();
		
		assertNotNull(request.getRequestId(), REQUEST_ID);
		validateOtherFields(request);
	}
	
	private void setOtherFields(CertificateRevocationRequestBuilder builder) {
		builder.setCertificate(CERTIFICATE)
			.setDescription(DESCRIPTION)
			.setDistinguishedName(DN)
			.setValidityEnd(END_DATE)
			.setLegalNotice(LEGAL_NOTICE)				
			.setValidityStart(START_DATE)
			.setOperator(new EntityBuilder()
				.setFunction(OPERATOR_FUNCTION)
				.setName(OPERATOR_NAME)
				.setPhone(OPERATOR_PHONE)
				.toEntityType()
			);
	}

	private void validateOtherFields(CertificateRevocationRequestType request) {
		assertEquals(request.getCertificate(), CERTIFICATE);
		assertEquals(request.getDescription(), DESCRIPTION);
		assertEquals(request.getDistinguishedName(), DN);
		assertEquals(request.getValidityEnd(), createXmlGregorianCalendar(END_DATE));
		assertEquals(request.getLegalNotice(), LEGAL_NOTICE);
		assertEquals(request.getOperator().getFunction(), OPERATOR_FUNCTION);
		assertEquals(request.getOperator().getName(), OPERATOR_NAME);
		assertEquals(request.getOperator().getPhone(), OPERATOR_PHONE);
		assertEquals(request.getValidityStart(), createXmlGregorianCalendar(START_DATE));		
	}
}
