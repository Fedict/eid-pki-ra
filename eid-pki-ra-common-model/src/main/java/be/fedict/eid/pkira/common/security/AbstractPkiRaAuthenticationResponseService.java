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
	public void validateServiceCertificate(SamlAuthenticationPolicy authenticationPolicy,
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

			log.info("Actual fingerprint: " + Hex.encodeHexString(fp));

			String[] fingerprintConfig = getFingerprints();

			if (fingerprintConfig == null || fingerprintConfig.length == 0) {
				log.warn("No fingerprint given");
				return;
			}

			boolean ok = false;
			Hex hex = new Hex();
			for (String fingerprint : fingerprintConfig) {
				log.info("Allowed fingerprint: " + fingerprint);
				byte[] fpConfig = (byte[]) hex.decode(fingerprint);
				ok |= java.util.Arrays.equals(fp, fpConfig);
			}

			if (!ok) {
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
		} catch (Throwable e) {
			log.error("Exception during service certificate validation", e);
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

	public abstract String[] getFingerprints();
}
