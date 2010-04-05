package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.ejb.Stateless;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.crypto.CSRParser;

/**
 * XKMS Service implementation.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(XKMSService.NAME)
public class XKMSServiceBean implements XKMSService {

	@In(value = CSRParser.NAME, create = true)
	private CSRParser csrParser;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void revoke(String certificate) throws ContractHandlerBeanException {
		// TODO implement me when we have an XKMS service
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sign(String csr) throws ContractHandlerBeanException {
		return createCertificate(csr);
	}

	private String createCertificate(String csr) {
		try {
			String dn = csrParser.parseCSR(csr).getSubject();

			Date startDate = new Date();
			Date expiryDate = new Date(startDate.getTime() + 1000L * 3600 * 24 * 360);
			BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
			generator.initialize(512);
			KeyPair keyPair = generator.generateKeyPair();

			X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
			X500Principal dnName = new X500Principal(dn);

			certGen.setSerialNumber(serialNumber);
			certGen.setIssuerDN(dnName);
			certGen.setNotBefore(startDate);
			certGen.setNotAfter(expiryDate);
			certGen.setSubjectDN(dnName);
			certGen.setPublicKey(keyPair.getPublic());
			certGen.setSignatureAlgorithm("SHA1withRSA");

			X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC");

			StringWriter writer = new StringWriter();
			PEMWriter pemWriter = new PEMWriter(writer);
			pemWriter.writeObject(cert);
			pemWriter.flush();

			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error creating self-signed demo certificate", e);
		}
	}

	protected void setCsrParser(CSRParser csrParser) {
		this.csrParser = csrParser;
	}

}
