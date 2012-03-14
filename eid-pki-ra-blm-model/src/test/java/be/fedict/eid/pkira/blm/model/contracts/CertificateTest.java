package be.fedict.eid.pkira.blm.model.contracts;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.ca.CertificateChainCertificate;

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
