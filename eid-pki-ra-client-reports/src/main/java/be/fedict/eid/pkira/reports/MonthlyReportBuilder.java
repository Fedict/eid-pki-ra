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

import java.util.ArrayList;
import java.util.List;

import be.fedict.eid.pkira.generated.reports.MonthlyReportType;
import be.fedict.eid.pkira.generated.reports.ObjectFactory;
import be.fedict.eid.pkira.generated.reports.MonthlyReportType.Date;

/**
 * Builder for one monthly report.
 * 
 * @author Jan Van den Bergh
 */
public class MonthlyReportBuilder implements Builder<MonthlyReportType> {

	private int year, month;
	private List<CertificateAuthorityReportItemBuilder> certificateAuthorityReportItemBuilders = new ArrayList<CertificateAuthorityReportItemBuilder>();
	private List<CertificateDomainReportItemBuilder> certificateDomainReportItemBuilders = new ArrayList<CertificateDomainReportItemBuilder>();

	/**
	 * Sets the date in the format yyyy-mm.
	 */
	public MonthlyReportBuilder setDate(String date) {
		int index = date.indexOf('-');
		year = Integer.parseInt(date.substring(0, index));
		month = Integer.parseInt(date.substring(index + 1));

		return this;
	}

	public CertificateAuthorityReportItemBuilder newCertificateAuthorityReportBuilder() {
		CertificateAuthorityReportItemBuilder result = new CertificateAuthorityReportItemBuilder();
		certificateAuthorityReportItemBuilders.add(result);
		return result;
	}
	
	public CertificateDomainReportItemBuilder newCertificateDomainReportBuilder() {
		CertificateDomainReportItemBuilder result = new CertificateDomainReportItemBuilder();
		certificateDomainReportItemBuilders.add(result);
		return result;
	}

	@Override
	public MonthlyReportType toXmlType() {
		ObjectFactory objectFactory = new ObjectFactory();

		MonthlyReportType result = objectFactory.createMonthlyReportType();

		Date reportDate = objectFactory.createMonthlyReportTypeDate();
		result.setDate(reportDate);
		reportDate.setMonth(month);
		reportDate.setYear(year);

		for (CertificateAuthorityReportItemBuilder reportItemBuilder : certificateAuthorityReportItemBuilders) {
			result.getCertificateAuthority().add(reportItemBuilder.toXmlType());
		}
		
		for (CertificateDomainReportItemBuilder reportItemBuilder : certificateDomainReportItemBuilders) {
			result.getCertificateDomain().add(reportItemBuilder.toXmlType());
		}

		return result;
	}

}
