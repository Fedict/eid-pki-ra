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

package be.fedict.eid.pkira.blm.model.contracts;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.ca.CertificateChainCertificate;

import static org.testng.Assert.assertEquals;

public class CertificateTest {

	@Test
	public void testGetCertificateZip() throws Exception {
		CertificateChainCertificate certificateChainCertificate2 = new CertificateChainCertificate();
		certificateChainCertificate2.setCertificateData("chain2\n");

		CertificateChainCertificate certificateChainCertificate1 = new CertificateChainCertificate();
		certificateChainCertificate1.setCertificateData("chain1\n");
		certificateChainCertificate1.setIssuer(certificateChainCertificate2);

		Certificate certificate = new Certificate();
		certificate.setX509("x509test\n");
		certificate.setCertificateChainCertificate(certificateChainCertificate1);

		File file = new File("target/testcertificate.zip");
		FileUtils.writeByteArrayToFile(file, certificate.getCertificateZip());
		
		ZipFile zipFile = new ZipFile(file);
		validateNextZipEntry(zipFile, "certificate.crt", "x509test\n");
		validateNextZipEntry(zipFile, "chain1.crt", "chain1\n");
		validateNextZipEntry(zipFile, "chain2.crt", "chain2\n");
	}

	protected void validateNextZipEntry(ZipFile zipFile, String fileName, String expectedText) throws IOException {
		ZipEntry entry = zipFile.getEntry(fileName);
		
		byte[] expectedBytes = expectedText.getBytes();
		byte[] actualBytes = new byte[expectedBytes.length];
		zipFile.getInputStream(entry).read(actualBytes);
		
		assertEquals(entry.getName(), fileName);
		assertEquals(entry.getSize(), expectedBytes.length);
		assertEquals(actualBytes, expectedBytes);
	}
}
