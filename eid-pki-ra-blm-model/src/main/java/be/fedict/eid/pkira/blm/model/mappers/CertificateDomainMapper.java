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
package be.fedict.eid.pkira.blm.model.mappers;

import java.util.Collection;
import java.util.List;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.generated.privatews.CertificateDomainWS;

/**
 * Mapper for certificate domains.
 * 
 * @author Jan Van den Bergh
 */
public interface CertificateDomainMapper {

	String NAME = "be.fedict.eid.pkira.blm.certificateDomainMapper";

	/**
	 * Maps a certificate domain on its web service counterpart.
	 */
	CertificateDomainWS map(CertificateDomain domain);
	
	/**
	 * Maps a collection of certificate domain on its web service counterpart.
	 */
	List<CertificateDomainWS> map(Collection<CertificateDomain> domains);
}
