/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
package be.fedict.eid.pkira.contracts;

import static be.fedict.eid.pkira.contracts.util.JAXBUtil.getObjectFactory;

import java.util.List;

import be.fedict.eid.pkira.generated.contracts.CertificateSigningResponseType;

/**
 * @author Jan Van den Bergh
 */
public class CertificateSigningResponseBuilder extends AbstractResponseBuilder<CertificateSigningResponseBuilder> {

	/**
	 * Create a new certificate revocation response builder that automatically
	 * generates the response id.
	 */
	public CertificateSigningResponseBuilder() {
		super();
	}

	/**
	 * Create a new certificate revocation response builder using the given
	 * response id.
	 */
	public CertificateSigningResponseBuilder(String responseId) {
		super(responseId);
	}

	public CertificateSigningResponseType toResponseType() {
		CertificateSigningResponseType response = getObjectFactory().createCertificateSigningResponseType();
		fillInResponseType(response);
		return response;
	}

	public CertificateSigningResponseType toResponseType(int certificateId, List<String> certificatesAsPEM) {
		CertificateSigningResponseType response = getObjectFactory().createCertificateSigningResponseType();
		fillInResponseType(response);
		if (certificateId >= 0) {
			response.setCertificateID(certificateId);
		}
		if (certificatesAsPEM != null) {
			response.getCertificatePEM().addAll(certificatesAsPEM);
		}
		return response;
	}
}
