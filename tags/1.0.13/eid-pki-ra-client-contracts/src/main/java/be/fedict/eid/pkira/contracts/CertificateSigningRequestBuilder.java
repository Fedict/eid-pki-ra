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

import java.math.BigInteger;
import java.util.List;

import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateTypeType;

/**
 * Request builder to build certificate signing contracts.
 * 
 * @author Jan Van den Bergh
 */
public class CertificateSigningRequestBuilder extends AbstractRequestBuilder<CertificateSigningRequestBuilder> {

	public CertificateTypeType certificateType;
	public String csr;
	public String distinguishedName;
	public int validityPeriodMonths;
	private List<String> alternativeNames;

	/**
	 * Creates a certificate revocation request builder setting a random id.
	 */
	public CertificateSigningRequestBuilder() {
		super();
	}

	/**
	 * Creates a certificate revocation request builder setting the specified
	 * request id.
	 */
	public CertificateSigningRequestBuilder(String requestId) {
		super(requestId);
	}

	public CertificateSigningRequestBuilder setCertificateType(CertificateTypeType certificateType) {
		this.certificateType = certificateType;
		return this;
	}

	public CertificateSigningRequestBuilder setCsr(String csr) {
		this.csr = csr;
		return this;
	}

	public CertificateSigningRequestBuilder setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
		return this;
	}

	public CertificateSigningRequestBuilder setValidityPeriodMonths(int validityPeriodMonths) {
		this.validityPeriodMonths = validityPeriodMonths;
		return this;
	}

	public CertificateSigningRequestBuilder setAlternativeNames(List<String> alternativeNames) {
		this.alternativeNames = alternativeNames;
		return this;
	}

	/**
	 * Builds the request type.
	 */
	public CertificateSigningRequestType toRequestType() {
		CertificateSigningRequestType requestType = getObjectFactory().createCertificateSigningRequestType();
		super.fillInRequestType(requestType);

		requestType.setCertificateType(certificateType);
		requestType.setCSR(csr);
		requestType.setDistinguishedName(distinguishedName);
		if (alternativeNames != null) {
			requestType.getAlternativeName().addAll(alternativeNames);
		}
		requestType.setValidityPeriodMonths(BigInteger.valueOf(validityPeriodMonths));

		return requestType;
	}

}
