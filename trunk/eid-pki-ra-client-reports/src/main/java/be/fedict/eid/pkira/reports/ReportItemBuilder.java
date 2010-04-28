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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import be.fedict.eid.pkira.generated.reports.DetailsType;
import be.fedict.eid.pkira.generated.reports.ObjectFactory;
import be.fedict.eid.pkira.generated.reports.ReportItemType;
import be.fedict.eid.pkira.generated.reports.SuccessFailureCountType;
import be.fedict.eid.pkira.generated.reports.SummaryType;

/**
 * @author Jan Van den Bergh
 */
public class ReportItemBuilder implements Builder<ReportItemType> {

	private String name;
	private int signingRequestSuccesses, signingRequestFailures, revocationRequestSuccesses, revocationRequestFailures;
	private List<DetailBuilder> detailBuilders = new ArrayList<DetailBuilder>();

	public ReportItemBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ReportItemBuilder withRequestCounts(int successes, int failures) {
		this.signingRequestSuccesses = successes;
		this.signingRequestFailures = failures;
		return this;
	}
	
	public ReportItemBuilder withRevocationCounts(int successes, int failures) {
		this.revocationRequestSuccesses = successes;
		this.revocationRequestFailures = failures;
		return this;
	}
	
	public DetailBuilder addDetailBuilder() {
		DetailBuilder result = new DetailBuilder();
		detailBuilders.add(result);
		return result;
	}

	@Override
	public ReportItemType build() {
		ObjectFactory objectFactory = new ObjectFactory();
		
		ReportItemType result = objectFactory.createReportItemType();
		result.setName(name);
		
		SummaryType summary = objectFactory.createSummaryType();
		result.setSummary(summary);
		summary.setRevocations(createSuccessFailureCountType(revocationRequestSuccesses, revocationRequestFailures));
		summary.setRequests(createSuccessFailureCountType(signingRequestSuccesses, signingRequestFailures));
		summary.setTotal(createSuccessFailureCountType(revocationRequestSuccesses+signingRequestSuccesses, revocationRequestFailures+signingRequestFailures));		
		
		DetailsType details = objectFactory.createDetailsType();		
		result.setDetails(details );
		for(DetailBuilder detailBuilder: detailBuilders) {
			details.getDetail().add(detailBuilder.build());
		}

		return result;
	}
	
	protected SuccessFailureCountType createCountType(int successes, int failures) {
		SuccessFailureCountType result = new ObjectFactory().createSuccessFailureCountType();
		result.setSuccess(BigInteger.valueOf(successes));
		result.setFailure(BigInteger.valueOf(failures));
	
		return result;
	}

}
