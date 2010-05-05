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
package be.fedict.eid.pkira.blm.model.reporting;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;

/**
 * @author Jan Van den Bergh
 *
 */
public interface ReportManager {

	public static final String NAME = "be.fedict.eid.pkira.blm.reportManager";
	
	/**
	 * Log a report entry line.
	 * @param contract contract to log.
	 * @param success if the contract was processed successfully.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	void addLineToReport(AbstractContract contract, boolean success);
	
	/**
	 * Creates a new report.
	 * @param startMonthYear month to start.
	 * @param endMonthYear month to end.
	 * @param showCertificateAuthorities show CA in report?
	 * @param showCertificateDomains show CD in report?
	 * @return the report as XML string.
	 */
	String generateReport(String startMonthYear, String endMonthYear, boolean showCertificateAuthorities, boolean showCertificateDomains);

}
