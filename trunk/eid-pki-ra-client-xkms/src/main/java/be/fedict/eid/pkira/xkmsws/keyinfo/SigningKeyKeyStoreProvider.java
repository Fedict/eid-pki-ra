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
package be.fedict.eid.pkira.xkmsws.keyinfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;

import be.fedict.eid.pkira.xkmsws.XMLSigningException;

/**
 * @author Jan Van den Bergh
 */
public class SigningKeyKeyStoreProvider implements SigningKeyProvider {

	public static final String PARAMETER_KEYSTORE_URL = "signing.keystore.url";
	public static final String PARAMETER_KEYSTORE_ENTRYNAME = "signing.keystore.entry";
	public static final String PARAMETER_KEYSTORE_PASSWORD = "signing.keystore.password";
	public static final String PARAMETER_KEYSTORE_ENTRY_PASSWORD = "signing.keystore.entry.password";

	private Map<String, String> parameters = Collections.emptyMap();
	private PrivateKeyEntry entry;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public X509Certificate getCertificate() throws XMLSigningException {
		return (X509Certificate) getEntry().getCertificate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrivateKey getPrivateKey() throws XMLSigningException {
		return getEntry().getPrivateKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
		this.entry = null;
	}

	private PrivateKeyEntry getEntry() throws XMLSigningException {
		if (entry == null) {
			String keystoreUrl = getParameter(PARAMETER_KEYSTORE_URL);
			try {
				URL url = new URL(keystoreUrl);
				String entryName = getParameter(PARAMETER_KEYSTORE_ENTRYNAME);
				String keystorePassword = getParameter(PARAMETER_KEYSTORE_PASSWORD);
				String entryPassword = getParameter(PARAMETER_KEYSTORE_ENTRY_PASSWORD);

				KeyStore keyStore = KeyStore.getInstance("JKS");

				keyStore.load(url.openStream(), keystorePassword.toCharArray());
				PasswordProtection passwordProtection = new PasswordProtection(entryPassword.toCharArray());
				entry = (PrivateKeyEntry) keyStore.getEntry(entryName, passwordProtection);
			} catch (MalformedURLException e) {
				throw new XMLSigningException("Invalid keystore URL: " + keystoreUrl);
			} catch (GeneralSecurityException e) {
				throw new XMLSigningException("Security exception while getting private key to sign.", e);
			} catch (IOException e) {
				throw new XMLSigningException("Error opening keystore.", e);
			}
		}

		return entry;
	}

	private String getParameter(String parameterName) throws XMLSigningException {
		String result = parameters.get(parameterName);
		if (result == null) {
			throw new XMLSigningException("Missing parameter: " + parameterName);
		}
		return result;
	}

}
