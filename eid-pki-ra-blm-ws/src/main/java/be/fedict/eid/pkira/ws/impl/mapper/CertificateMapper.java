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
package be.fedict.eid.pkira.ws.impl.mapper;

import be.fedict.eid.pkira.blm.model.domain.Certificate;
import be.fedict.eid.pkira.blm.model.domain.CertificateType;
import be.fedict.eid.pkira.generated.privatews.CertificateTypeWS;
import be.fedict.eid.pkira.generated.privatews.CertificateWS;

/**
 * Mapper for certificates from WS to model and back.
 * 
 * @author Jan Van den Bergh
 */
public interface CertificateMapper {

	public static final String NAME = "certificateMapper";
	
	/**
	 * Maps a certificate type to the WS version.
	 */
	CertificateTypeWS map(CertificateType certificateType);
	
	/**
	 * Maps a certificate type from the WS version.
	 */
	CertificateType map(CertificateTypeWS certificateTypeWS);
	
	/**
	 * Maps a certificate to the WS version.
	 * @param includeX509 TODO
	 */
	CertificateWS map(Certificate certificate, boolean includeX509);
}
