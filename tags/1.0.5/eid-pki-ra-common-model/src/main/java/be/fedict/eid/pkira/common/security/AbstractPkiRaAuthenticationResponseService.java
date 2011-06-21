package be.fedict.eid.pkira.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import be.fedict.eid.idp.common.SamlAuthenticationPolicy;
import be.fedict.eid.idp.sp.protocol.saml2.spi.AuthenticationResponseService;

public abstract class AbstractPkiRaAuthenticationResponseService implements AuthenticationResponseService {
	
	public static final String NAME = "be.fedict.eid.pkira.common.PkiRaAuthenticationResponseService";	
	
	@Logger
	private static Log log;
	
	@Override
	public boolean requiresResponseSignature() {
		return true;
	}

	@Override
	public void validateServiceCertificate(
			SamlAuthenticationPolicy authenticationPolicy,
			List<X509Certificate> certificateChain) throws SecurityException {
		if (certificateChain == null || certificateChain.size() == 0) {
			throw new SecurityException("Missing certificate chain");
		}
		X509Certificate certificate = certificateChain.get(0);

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");

			md.update(certificate.getEncoded());
			byte[] fp = md.digest();
			
			String fingerprintConfig = getFingerprint();
			
			if(fingerprintConfig == null || "".equals(fingerprintConfig)){
				log.warn("No fingerprint given");
				return;
			}
			
			Hex hex = new Hex();
			byte[] fpConfig = (byte[]) hex.decode(fingerprintConfig);
			
			if(!java.util.Arrays.equals(fp, fpConfig)){
				log.error("Signatures not correct.");
				throw new SecurityException("Signatures not correct.");
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("No Such Algorithm", e);
			throw new SecurityException(e.getMessage());
		} catch (CertificateEncodingException e) {
			log.error("Certificate Encoding Exception", e);
			throw new SecurityException(e.getMessage());
		} catch (DecoderException e) {
			log.error("Fingerprint decode problem", e);
			throw new SecurityException(e.getMessage());
		}	
	}

	@Override
	public SecretKey getAttributeSecretKey() {
		return null;
	}

	@Override
	public PrivateKey getAttributePrivateKey() {
		return null;
	}

	@Override
	public abstract int getMaximumTimeOffset();
	
	public abstract String getFingerprint();
}
