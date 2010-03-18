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
package be.fedict.eid.pkira.blm.model.certificatedomain;


import javax.ejb.Local;

import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

/**
 * @author Jan Van den Bergh
 */
@Local
public interface CertificateDomainManager {

	static final String NAME = "certificateDomainManager";

	/**
	 * @param domain the domain to register.
	 * @throws DistinguishedNameOverlapsException
	 *             if the DN overlaps with an already existing one.
	 * @throws InvalidCertificateDomainNameException if the name already exists.
	 * @throws NoCertificateTypesSelectedException if no certificate types have been checked in the domain.
	 */
	void saveCertificateDomain(CertificateDomain domain) throws InvalidDistinguishedNameException, DistinguishedNameOverlapsException, InvalidCertificateDomainNameException, NoCertificateTypesSelectedException;

}
