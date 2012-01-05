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
package be.fedict.eid.pkira.blm.model.mappers;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.math.BigInteger;
import java.util.Date;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;

/**
 * @author Jan Van den Bergh
 */
public class CertificateMapperBeanTest {

	private static final CertificateType TEST_TYPE = CertificateType.CLIENT;
	private static final String TEST_DN = "DN";
	private static final String TEST_ISSUER = "Issuser";
	private static final String TEST_REQUESTER = "Requester";
	private static final BigInteger TEST_SERIALNUMBER = BigInteger.TEN;
	private static final Date TEST_END = new Date();
	private static final Date TEST_START = new Date(TEST_END.getTime() - 10000);
	private static final String TEST_X509 = "X.509";

	private final CertificateMapperBean bean = new CertificateMapperBean();

	/**
	 * Test method for
	 * {@link be.fedict.eid.pkira.blm.model.mappers.CertificateMapperBean#map(be.fedict.eid.pkira.blm.model.contracts.CertificateType)}
	 * .
	 */
	@Test
	public void testMapCertificateType() {
		for (CertificateType certificateType : CertificateType.values()) {
			CertificateTypeWS certificateTypeWS = bean.map(certificateType);
			assertNotNull(certificateTypeWS);
			assertEquals(certificateTypeWS.name(), certificateType.name());
		}

		assertNull(bean.map((CertificateType) null));
	}

	/**
	 * Test method for
	 * {@link be.fedict.eid.pkira.blm.model.mappers.CertificateMapperBean#map(be.fedict.eid.pkira.generated.privatews.CertificateTypeWS)}
	 * .
	 */
	@Test
	public void testMapCertificateTypeWS() {
		for (CertificateTypeWS certificateTypeWS : CertificateTypeWS.values()) {
			CertificateType certificateType = bean.map(certificateTypeWS);
			assertNotNull(certificateType);
			assertEquals(certificateType.name(), certificateTypeWS.name());
		}

		assertNull(bean.map((CertificateTypeWS) null));
	}

	/**
	 * Test method for
	 * {@link be.fedict.eid.pkira.blm.model.mappers.CertificateMapperBean#map(be.fedict.eid.pkira.blm.model.contracts.Certificate, boolean)}
	 * .
	 */
	@Test
	public void testMapCertificate() {
		Certificate certificate = createTestCertificate();
		CertificateWS certificateWS = bean.map(certificate, true);

		assertNotNull(certificateWS);
		validateCertificateWS(certificateWS);
	}

	@Test
	public void testMapCertificateNull() {
		assertNull(bean.map(null, true));
	}

	private void validateCertificateWS(CertificateWS certificateWS) {
		assertEquals(certificateWS.getCertificateType(), bean.map(TEST_TYPE));
		assertEquals(certificateWS.getDistinguishedName(), TEST_DN);
		assertEquals(certificateWS.getIssuer(), TEST_ISSUER);
		assertEquals(certificateWS.getRequesterName(), TEST_REQUESTER);
		assertEquals(certificateWS.getSerialNumber(), TEST_SERIALNUMBER.toString());
		assertEquals(certificateWS.getValidityEnd().toGregorianCalendar().getTime().getTime(), TEST_END.getTime());
		assertEquals(certificateWS.getValidityStart().toGregorianCalendar().getTime().getTime(), TEST_START.getTime());
	}

	private Certificate createTestCertificate() {
		Certificate certificate = new Certificate();
		certificate.setCertificateType(TEST_TYPE);
		certificate.setDistinguishedName(TEST_DN);
		certificate.setIssuer(TEST_ISSUER);
		certificate.setRequesterName(TEST_REQUESTER);
		certificate.setSerialNumber(TEST_SERIALNUMBER);
		certificate.setValidityEnd(TEST_END);
		certificate.setValidityStart(TEST_START);
		certificate.setX509(TEST_X509);
		return certificate;
	}

}
