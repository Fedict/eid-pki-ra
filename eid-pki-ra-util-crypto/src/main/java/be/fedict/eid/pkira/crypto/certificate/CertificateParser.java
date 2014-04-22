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
package be.fedict.eid.pkira.crypto.certificate;

import be.fedict.eid.pkira.crypto.exception.CryptoException;

/**
 * Parser to parse the certificate.
 * 
 * @author Jan Van den Bergh
 */
public interface CertificateParser {

	public final static String NAME = "be.fedict.eid.pkira.crypto.certificate.certificateParser";
	
	/**
	 * Parses the certificate and returns a list of fields in it.
	 * @param certificate certificate to parse.
	 * @return information extracted from the certificate.
	 * @throws CryptoException when the certificate could not be successfully parsed.
	 */
	CertificateInfo parseCertificate(String certificate) throws CryptoException;
	
	/**
	 * Parses the certificate and returns a list of fields in it.
	 * @param certificate certificate to parse.
	 * @return information extracted from the certificate.
	 * @throws CryptoException when the certificate could not be successfully parsed.
	 */
	CertificateInfo parseCertificate(byte[] certificate) throws CryptoException;
}
