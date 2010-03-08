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
package be.fedict.eid.pkira.blm.model.domain;

import java.util.List;

import javax.ejb.Local;

/**
 * Interface to the repository used to store and retrieve domain objects.
 * 
 * @author Jan Van den Bergh
 */
@Local
public interface DomainRepository {

	public static final String NAME = "domainRepository";

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
	public List<Certificate> findAllCertificates(String userRRN);
}
