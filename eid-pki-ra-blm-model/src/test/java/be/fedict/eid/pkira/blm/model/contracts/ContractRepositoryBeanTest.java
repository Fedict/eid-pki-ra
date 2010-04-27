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
package be.fedict.eid.pkira.blm.model.contracts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateType;
import be.fedict.eid.pkira.blm.model.contracts.ContractRepositoryBean;

/**
 * @author Jan Van den Bergh
 *
 */
public class ContractRepositoryBeanTest extends DatabaseTest {
	
	private static final CertificateType VALID_CERTIFICATE_TYPE = CertificateType.CODE;
	private static final Date VALID_ENDDATE = new Date(System.currentTimeMillis()+1000*3600);
	private static final String VALID_ISSUER = "ISSUER";
	private static final String VALID_REQUESTER = "REQUESTER";
	private static final BigInteger VALID_SERIALNUMBER = BigInteger.TEN;
	private static final Date VALID_STARTDATE = new Date();
	private static final String VALID_DN = "DISTINGUISHED_NAME";
	private static final String VALID_X509 = "X509";
	private static final String VALID_CONTRACT = "CONTRACT";
	private static final Integer VALID_VALIDITY = 15;

	private ContractRepositoryBean bean;
	
	private CertificateDomain certificateDomain;
	
	@BeforeMethod
	public void setup() {
		bean = new ContractRepositoryBean();
		bean.setEntityManager(getEntityManager());
		
		certificateDomain = loadObject(CertificateDomain.class, TEST_CERTIFICATE_DOMAIN_ID);
	}
	
//	@Test(dependsOnMethods="testPersistCertificate")
//	public void testFindAllCertificates() {
//		List<Certificate> certificates = bean.findAllCertificates(VALID_ISSUER, null);
//		
//		assertNotNull(certificates);
//		assertEquals(certificates.size(), 1);
//		validateCertificate(certificates.get(0));
//	}
	
	@Test
	public void testPersistCertificate() {
		forceCommit();
		
		Certificate certificate = createValidCertificate();
		bean.persistCertificate(certificate);			
		assertNotNull(certificate.getId());
	}
	
	@Test(dependsOnMethods="testPersistCertificate", expectedExceptions=RuntimeException.class)
	public void testPersistCertificateAgain() {
		Certificate certificate = createValidCertificate();
		bean.persistCertificate(certificate);
		fail("Expected an exception");
	}

	@Test
	public void testPersistContract() {
		forceCommit();
		
		CertificateSigningContract contract = createValidCertificateSigningContract();
		bean.persistContract(contract);
		
		assertNotNull(contract.getId());
	}

	private CertificateSigningContract createValidCertificateSigningContract() {
		CertificateSigningContract contract = new CertificateSigningContract();
		contract.setCreationDate(new Date());
		contract.setCertificateType(VALID_CERTIFICATE_TYPE);
		contract.setContractDocument(VALID_CONTRACT);
		contract.setRequester(VALID_REQUESTER);
		contract.setSubject(VALID_DN);
		contract.setValidityPeriodMonths(VALID_VALIDITY);
		contract.setCertificateDomain(certificateDomain);
		
		return contract;
	}

	@Test
	public void testRemoveCertificate() {
		Certificate certificate = bean.findCertificate(VALID_ISSUER, VALID_SERIALNUMBER);
		assertNotNull(certificate);
		
		bean.removeCertificate(certificate);
		
		certificate = bean.findCertificate(VALID_ISSUER, VALID_SERIALNUMBER);
		assertNull(certificate);
	}

	private Certificate createValidCertificate() {
		Certificate certificate = new Certificate();		
		certificate.setIssuer(VALID_ISSUER);
		certificate.setCertificateType(VALID_CERTIFICATE_TYPE);
		certificate.setRequesterName(VALID_REQUESTER);
		certificate.setSerialNumber(VALID_SERIALNUMBER);
		certificate.setDistinguishedName(VALID_DN);
		certificate.setValidityEnd(VALID_ENDDATE);
		certificate.setValidityStart(VALID_STARTDATE);
		certificate.setX509(VALID_X509);
		certificate.setCertificateDomain(certificateDomain);
		
		return certificate;
	}

	private void validateCertificate(Certificate certificate) {
		assertEquals(certificate.getIssuer() ,VALID_ISSUER );
		assertEquals(certificate.getCertificateType() ,VALID_CERTIFICATE_TYPE);
		assertEquals(certificate.getRequesterName() ,VALID_REQUESTER);
		assertEquals(certificate.getSerialNumber() ,VALID_SERIALNUMBER);
		assertEquals(certificate.getDistinguishedName() ,VALID_DN);
		assertEquals(certificate.getValidityEnd() ,VALID_ENDDATE);
		assertEquals(certificate.getValidityStart() ,VALID_STARTDATE );
		assertEquals(certificate.getX509() ,VALID_X509);
		assertNotNull(certificate.getId());		
	}
}
