/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

package be.fedict.eid.pkira.blm.model.certificatedomain;


/**
 * @author Bram Baeyens
 */
public class CertificateDomainHomeTest /*extends SeamTest*/ {

//	private static final String DN_EXPRESSION = "cdTestDnExpression";
//	private static final String DN_EXPRESSION_2 = "cdTestDnExpression2";
//	private static final String DN_EXPRESSION_UNKNOWN = "cdTestDnExpressionUnknown";
//	private static final String NAME = "cdTestName";
//	private static final String NAME_UNKNOWN = "cdTestNameUnknown";
//
//	@Test
//	public void persist() throws Exception {
//		new ComponentTest() {
//			@Override
//			protected void testComponents() throws Exception {
//				setValue("#{certificateDomain.name}", NAME);
//				setValue("#{certificateDomain.dnExpression}", DN_EXPRESSION);
//				setValue("#{certificateDomain.serverCertificate}", true);
//				setValue("#{certificateDomain.clientCertificate}", false);
//				setValue("#{certificateDomain.codeSigningCertificate}", true);
//				assert invokeMethod("#{certificateDomainHome.persist}").equals("created");
//				assert getValue("#{certificateDomain.id}") != null;
//			}
//		}.run();
//	}

	// @Test(dependsOnMethods = "persist", expectedExceptions =
	// PersistenceException.class)
	// public void nameConstraint() throws Exception {
	// home.setInstance(createCertificateDomain(DN_EXPRESSION_2, NAME));
	// home.persist();
	// fail("PersistenceException expected");
	// }
	//
	// @Test(dependsOnMethods = "persist")
	// public void findById() throws Exception {
	// home.setId(valid.getId());
	// CertificateDomain certificateDomain = home.find();
	// assertEquals(certificateDomain, valid);
	// }
	//
	// @Test(dependsOnMethods = "persist")
	// public void findByNonExistingId() throws Exception {
	// home.setId(Integer.valueOf(987654));
	// CertificateDomain certificateDomain = home.find();
	// assertNull(certificateDomain);
	// }
	//
	// @Test(dependsOnMethods = "persist")
	// public void findByName() throws Exception {
	// CertificateDomain certificateDomain = home.findByName(NAME);
	// assertEquals(certificateDomain, valid);
	// }
	//
	// @Test(dependsOnMethods = "persist")
	// public void findByNonExistingName() throws Exception {
	// assertNull(home.findByName(NAME_UNKNOWN));
	// }
	//
	// @Test(dependsOnMethods = "persist")
	// public void findByDnExpression() throws Exception {
	// CertificateDomain certificateDomain =
	// home.findByDnExpression(DN_EXPRESSION);
	// assertEquals(certificateDomain, valid);
	// }
	//
	// @Test(dependsOnMethods = "persist")
	// public void findByNonExistingDnExpression() throws Exception {
	// assertNull(home.findByDnExpression(DN_EXPRESSION_UNKNOWN));
	// }
	//
	// @Test(dependsOnMethods = "persist")
	// public void findByCertificateTypes() throws Exception {
	// Set<CertificateType> certificateTypes = new HashSet<CertificateType>();
	// for (CertificateType type : CertificateType.values()) {
	// certificateTypes.add(type);
	// }
	// List<CertificateDomain> domains =
	// home.findByCertificateTypes(certificateTypes);
	// assertNotNull(domains);
	// assertTrue(domains.size()>=1);
	// assertTrue(domains.contains(valid));
	// }

//	private CertificateDomain createCertificateDomain(String dnExpression,
//			String name) {
//		CertificateDomain certificateDomain = new CertificateDomain();
//		certificateDomain.setDnExpression(dnExpression);
//		certificateDomain.setName(name);
//		certificateDomain.setClientCertificate(true);
//		return certificateDomain;
//	}
}
