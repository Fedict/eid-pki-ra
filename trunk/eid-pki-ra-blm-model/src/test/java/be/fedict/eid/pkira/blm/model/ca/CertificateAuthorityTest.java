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

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.DatabaseTest;

/**
 * @author Jan Van den Bergh
 */
public class CertificateAuthorityTest extends DatabaseTest {

	private Integer caId;

	@Test
	public void testPersist() {
		CertificateAuthority ca = new CertificateAuthority();
		ca.setName("bla");
		ca.setXkmsUrl("http://ca.com/");
		ca.getParameters().put("abc", "def");
		ca.getParameters().put("def", "ghi");

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
