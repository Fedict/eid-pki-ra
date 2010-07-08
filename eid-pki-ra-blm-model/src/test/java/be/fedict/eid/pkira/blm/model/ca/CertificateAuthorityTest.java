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
package be.fedict.eid.pkira.blm.model.ca;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.util.BaseTest;

/**
 * @author Jan Van den Bergh
 */
public class CertificateAuthorityTest extends BaseTest {

	private Integer caId;

	@Mock
	CertificateAuthorityHome authorityHome;
	
	@BeforeMethod
	public void init(){
		MockitoAnnotations.initMocks(this);
		
		registerMock(CertificateAuthorityHome.NAME, authorityHome);
	}
	
	@Test
	public void testPersist() {
		
		Mockito.when(authorityHome.findByName(Mockito.anyString())).thenReturn(null);
		
		CertificateAuthority ca = new CertificateAuthority();
		
		CertificateAuthorityParameter cap1 = new CertificateAuthorityParameter();
		cap1.setCa(ca);
		cap1.setKey("abc");
		cap1.setValue("def");

		CertificateAuthorityParameter cap2 = new CertificateAuthorityParameter();
		cap2.setCa(ca);
		cap2.setKey("def");
		cap2.setValue("ghi");
		
		
		ca.setName("bla");
		ca.setXkmsUrl("http://ca.com/");	
		ca.getParameters().add(cap1);
		ca.getParameters().add(cap2);
		ca.setLegalNotice("Some legal notice");

		forceCommit();
		getEntityManager().persist(ca);
		
		this.caId = ca.getId();
	}

	@Test(dependsOnMethods="testPersist")
	public void testGetCA() {
		List<?> resultList = getEntityManager().
			createQuery("FROM CertificateAuthority WHERE id=:id").
			setParameter("id", caId).
			getResultList();

		assertNotNull(resultList);
		assertEquals(resultList.size(), 1);
		CertificateAuthority ca = (CertificateAuthority) resultList.get(0);
		assertEquals(ca.getParameters().size(), 2);
	}
	
	@Test
	public void getCAs() {
		List<?> resultList = getEntityManager().createQuery("FROM CertificateAuthority").getResultList();

		assertNotNull(resultList);
		assertEquals(resultList.size(), 2);
	}
}
