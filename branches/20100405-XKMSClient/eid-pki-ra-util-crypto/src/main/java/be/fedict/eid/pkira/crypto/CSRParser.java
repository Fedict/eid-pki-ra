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
package be.fedict.eid.pkira.crypto;

/**
 * CSR Parser to parse and validate CSRs.
 * 
 * @author Jan Van den Bergh
 */
public interface CSRParser {

	String NAME = "be.fedict.eid.pkira.crypto.csrParser";

	/**
	 * Parse and verify a CSR and return information extracted from it.
	 * 
	 * @param csr
	 *            the CSR to parse.
	 * @return the information.
	 * @throws CryptoException
	 *             if the CSR is invalid.
	 */
	CSRInfo parseCSR(String csr) throws CryptoException;

	/**
	 * Parse and verify a CSR and return information extracted from it.
	 * @param csr the CSR to parse.
	 * @return the information.
	 * @throws CryptoException if the CSR is invalid.
	 */
	CSRInfo parseCSR(byte[] csr) throws CryptoException;
}
