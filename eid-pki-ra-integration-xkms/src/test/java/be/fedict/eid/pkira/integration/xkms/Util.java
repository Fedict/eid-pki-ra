package be.fedict.eid.pkira.integration.xkms;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;

import be.fedict.eid.pkira.crypto.CSRInfo;

@SuppressWarnings("deprecation")
public class Util {

	public static final String DN_PREFIX = "CN=test-";
	public static final String DN_SUFFIX = ",O=Fedict,L=Brussel,ST=Brussel,C=BE";

	public static CSRInfo generateCSR() {
		try {
			// CSR Subject
			String subject = DN_PREFIX + System.currentTimeMillis() + DN_SUFFIX;
			X509Name xname = new X509Name(subject);

			// CSR Key Pair
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
			KeyPair generateKeyPair = generator.generateKeyPair();
			PrivateKey priv = generateKeyPair.getPrivate();
			PublicKey pub = generateKeyPair.getPublic();

			// CSR
			PKCS10CertificationRequest csr = new PKCS10CertificationRequest("MD5WithRSA", xname, pub, null, priv);
			return new CSRInfo(csr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
