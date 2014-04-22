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
package be.fedict.eid.pkira.reports;

import java.util.ArrayList;
import java.util.List;

import be.fedict.eid.pkira.generated.reports.MonthlyReportType;
import be.fedict.eid.pkira.generated.reports.ObjectFactory;

/**
 * Builder for one monthly report.
 * 
 * @author Jan Van den Bergh
 */
public class MonthlyReportBuilder implements Builder<MonthlyReportType> {

	private int year, month;
	private List<ReportItemBuilder> certificateAuthorityReportItemBuilders = new ArrayList<ReportItemBuilder>();
	private List<ReportItemBuilder> certificateDomainReportItemBuilders = new ArrayList<ReportItemBuilder>();

	/**
	 * Sets the date in the format yyyy-mm.
	 */
	public MonthlyReportBuilder withDate(String date) {
		int index = date.indexOf('-');
		year = Integer.parseInt(date.substring(0, index));
		month = Integer.parseInt(date.substring(index + 1));

		return this;
	}

	public ReportItemBuilder newCertificateAuthorityReportBuilder() {
		ReportItemBuilder result = new ReportItemBuilder();
		certificateAuthorityReportItemBuilders.add(result);
		return result;
	}
	
	public ReportItemBuilder newCertificateDomainReportBuilder() {
		ReportItemBuilder result = new ReportItemBuilder();
		certificateDomainReportItemBuilders.add(result);
		return result;
	}

	@Override
	public MonthlyReportType build() {
		ObjectFactory objectFactory = new ObjectFactory();

		MonthlyReportType result = objectFactory.createMonthlyReportType();
		result.setMonth(month);
		result.setYear(year);

		for (ReportItemBuilder reportItemBuilder : certificateAuthorityReportItemBuilders) {
			result.getCertificateAuthority().add(reportItemBuilder.build());
		}
		
		for (ReportItemBuilder reportItemBuilder : certificateDomainReportItemBuilders) {
			result.getCertificateDomain().add(reportItemBuilder.build());
		}

		return result;
	}

}
