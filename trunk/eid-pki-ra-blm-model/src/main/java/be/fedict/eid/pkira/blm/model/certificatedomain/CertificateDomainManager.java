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


import java.util.Set;

import javax.ejb.Local;

import be.fedict.eid.pkira.blm.model.domain.CertificateType;
import be.fedict.eid.pkira.dnfilter.InvalidDistinguishedNameException;

/**
 * @author Jan Van den Bergh
 */
@Local
public interface CertificateDomainManager {

	static final String NAME = "certificateDomainHandler";

	/**
	 * @param name
	 *            name of the certificate domain.
	 * @param caId
	 *            id of the CA for the certificate domain.
	 * @param dnExpression
	 *            expression describing the allowed DNs.
	 * @param types
	 *            certificate type for the domain
	 * @return the created domain.
	 * @throws InvalidDistinguishedNameException
	 *             if the DN expressions if invalid.
	 * @throws DistinguishedNameOverlapsException
	 *             if the DN overlaps with an already existing one.
	 * @throws DuplicateCertificateDomainNameException if the name already exists.
	 */
	CertificateDomain registerCertificateDomain(String name, String caId, String dnExpression,
			Set<CertificateType> types) throws InvalidDistinguishedNameException, DistinguishedNameOverlapsException, DuplicateCertificateDomainNameException;

}
