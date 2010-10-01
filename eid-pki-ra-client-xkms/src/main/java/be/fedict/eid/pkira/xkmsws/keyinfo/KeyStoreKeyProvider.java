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
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import be.fedict.eid.pkira.xkmsws.XMLSigningException;

/**
 * @author Jan Van den Bergh
 */
public class KeyStoreKeyProvider extends KeyProviderBase implements KeyProvider {

	public static final String PARAMETER_KEYSTORE_URL = "signing.keystore.url";
	public static final String PARAMETER_KEYSTORE_ENTRYNAME = "signing.keystore.entry";
	public static final String PARAMETER_KEYSTORE_PASSWORD = "signing.keystore.password";
	public static final String PARAMETER_KEYSTORE_ENTRY_PASSWORD = "signing.keystore.entry.password";
	public static final String PARAMETER_KEYSTORE_TYPE = "signing.keystore.type";

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

	private PrivateKeyEntry getEntry() throws XMLSigningException {
		if (entry == null) {
			String keystoreUrl = getParameter(PARAMETER_KEYSTORE_URL);
			// String entryName = getParameter(PARAMETER_KEYSTORE_ENTRYNAME);
			String keystorePassword = getParameter(PARAMETER_KEYSTORE_PASSWORD);
			String entryPassword = getParameter(PARAMETER_KEYSTORE_ENTRY_PASSWORD);
			String keyStoreType = getParameter(PARAMETER_KEYSTORE_TYPE);

			try {
				URL url = new URL(keystoreUrl);
				KeyStore keyStore = KeyStore.getInstance(keyStoreType);

				keyStore.load(url.openStream(), keystorePassword.toCharArray());
				PasswordProtection passwordProtection = new PasswordProtection(entryPassword.toCharArray());

				String entryName = keyStore.aliases().nextElement();
				entry = (PrivateKeyEntry) keyStore.getEntry(entryName, passwordProtection);
				if (entry == null) {
					throw new XMLSigningException("Entry with name " + entryName + " not found in the keystore.");
				}
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

	@Override
	public void setParameters(Map<String, String> parameters) {
		super.setParameters(parameters);
		entry = null;
	}
}
