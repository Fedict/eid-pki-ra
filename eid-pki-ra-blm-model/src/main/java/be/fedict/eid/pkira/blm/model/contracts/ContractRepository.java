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
package be.fedict.eid.pkira.blm.model.contracts;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Local;

/**
 * Interface to the repository used to store and retrieve domain objects.
 * 
 * @author Jan Van den Bergh
 */
@Local
public interface ContractRepository {

	public static final String NAME = "be.fedict.eid.pkira.blm.contractRepository";

	/**
	 * Persists a contract.
	 * 
	 * @param contract
	 *            contract to persist.
	 */
	public void persistContract(AbstractContract contract);

	/**
	 * Persists a certificate.
	 * 
	 * @param certificate
	 *            certificate to persist.
	 */
	public void persistCertificate(Certificate certificate);

	/**
	 * Retrieves a list of all certificates.
	 */
	public List<Certificate> findAllCertificates(String userRRN, String certificateDomainID);

	/**
	 * Finds a certificate using its issuer's DN and its serial number.
	 * @param issuer DN of the issuer.
	 * @param serialNumber serial number of the certificate.
	 * @return the certificate (if it can be found).
	 */
	public Certificate findCertificate(String issuer, BigInteger serialNumber);

	/**
	 * Find a certificate using its certificateID
	 * @param certificateID the certificateID
	 * @return the certificate
	 */
	public Certificate findCertificate(int certificateID);
	
	/**
	 * Removes a certificate from the database.
	 * @param certificate the certificate to remove.
	 */
	public void removeCertificate(Certificate certificate);
}
