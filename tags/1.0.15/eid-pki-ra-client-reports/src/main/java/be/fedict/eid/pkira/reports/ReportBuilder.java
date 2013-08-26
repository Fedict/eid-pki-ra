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
package be.fedict.eid.pkira.reports;

import java.util.ArrayList;
import java.util.List;

import be.fedict.eid.pkira.generated.reports.ObjectFactory;
import be.fedict.eid.pkira.generated.reports.ReportType;

/**
 * Builder to easily create ReportTypes.
 * 
 * @author Jan Van den Bergh
 */
public class ReportBuilder implements Builder<ReportType> {

	private List<MonthlyReportBuilder> monthlyReportBuilders = new ArrayList<MonthlyReportBuilder>();
	
	public MonthlyReportBuilder newMonthlyReport() {
		MonthlyReportBuilder result = new MonthlyReportBuilder();
		monthlyReportBuilders.add(result);
		return result;
	}

	public ReportType build() {
		ReportType result = new ObjectFactory().createReportType();
		for(MonthlyReportBuilder monthlyReportBuilder: monthlyReportBuilders) {
			result.getMonthlyReport().add(monthlyReportBuilder.build());
		}
		
		return result;
	}
}
