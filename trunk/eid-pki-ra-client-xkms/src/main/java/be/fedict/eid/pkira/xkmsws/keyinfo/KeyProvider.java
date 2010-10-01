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

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import be.fedict.eid.pkira.xkmsws.XMLSigningException;

/**
 * Interface used to get the key / certificate to sign the XKMS message.
 * 
 * @author Jan Van den Bergh
 */
public interface KeyProvider {

	/**
	 * Get the X509 certificate that belongs to the key.
	 * @throws XMLSigningException when the certificate could not be retrieved using the specified parameters.
	 */
	public X509Certificate getCertificate() throws XMLSigningException;
	
	/**
	 * Get the private key that belongs to the key.
	 * @throws XMLSigningException when the key could not be retrieved using the specified parameters.
	 */
	public PrivateKey getPrivateKey() throws XMLSigningException;
	
	/**
	 * Returns the parameters used to get the key.
	 */
	public void setParameters(Map<String, String> parameters);
}
