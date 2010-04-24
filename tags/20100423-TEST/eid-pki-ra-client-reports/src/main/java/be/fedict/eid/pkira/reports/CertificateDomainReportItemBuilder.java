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
package be.fedict.eid.pkira.reports;

import static be.fedict.eid.pkira.reports.BuilderUtil.createSuccessFailureCountType;
import be.fedict.eid.pkira.generated.reports.CertificateDomainReportItemType;
import be.fedict.eid.pkira.generated.reports.ObjectFactory;

/**
 * @author Jan Van den Bergh
 */
public class CertificateDomainReportItemBuilder implements Builder<CertificateDomainReportItemType> {

	private String certificateDomainName;
	private int signingRequestSuccesses, signingRequestFailures;

	public CertificateDomainReportItemBuilder setCertificateDomainName(String certificateDomainName) {
		this.certificateDomainName = certificateDomainName;
		return this;
	}

	public CertificateDomainReportItemBuilder setSigningRequestCounts(int successes, int failures) {
		this.signingRequestSuccesses = successes;
		this.signingRequestFailures = failures;
		return this;
	}

	@Override
	public CertificateDomainReportItemType toXmlType() {
		CertificateDomainReportItemType result = new ObjectFactory().createCertificateDomainReportItemType();
		result.setName(certificateDomainName);

		result.setSign(createSuccessFailureCountType(signingRequestSuccesses, signingRequestFailures));

		return result;
	}

}
