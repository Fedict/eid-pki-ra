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
import static be.fedict.eid.pkira.contracts.util.JAXBUtil.*;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationResponseType;


/**
 * @author Jan Van den Bergh
 *
 */
public class CertificateRevocationResponseBuilder extends AbstractResponseBuilder<CertificateRevocationResponseBuilder> {

	/**
	 * Create a new certificate revocation response builder that automatically generates the response id.
	 */
	public CertificateRevocationResponseBuilder() {
		super();
	}

	/**
	 * Create a new certificate revocation response builder using the given response id.
	 */
	public CertificateRevocationResponseBuilder(String responseId) {
		super(responseId);
	}

	public CertificateRevocationResponseType toResponseType() {
		CertificateRevocationResponseType response = getObjectFactory().createCertificateRevocationResponseType();
		fillInResponseType(response);
		return response;
	}
}
