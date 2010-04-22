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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracts.AbstractContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateRevocationContract;
import be.fedict.eid.pkira.blm.model.contracts.CertificateSigningContract;
import be.fedict.eid.pkira.blm.model.reporting.ReportEntry.ContractType;

/**
 * @author Jan Van den Bergh
 */
@Stateless
@Name(ReportManager.NAME)
public class ReportManagerBean implements ReportManager {

	@In(value = ReportEntryHome.NAME, create = true)
	private ReportEntryHome reportEntryHome;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void addLineToReport(AbstractContract contract, boolean success) {
		ReportEntry entry = reportEntryHome.getInstance();
		entry.setCertificateAuthorityName(contract.getCertificateDomain().getCertificateAuthority().getName());
		entry.setCertificateDomainName(contract.getCertificateDomain().getName());
		entry.setContract(contract);
		entry.setSuccess(success);
		entry.setMonth(new SimpleDateFormat("yyyy-MM").format(new Date()));
		entry.setContractType(mapToContractType(contract));

		reportEntryHome.persist();
	}

	private ContractType mapToContractType(AbstractContract contract) {
		if (contract instanceof CertificateSigningContract) {
			return ContractType.REQUEST;
		}
		if (contract instanceof CertificateRevocationContract) {
			return ContractType.REVOKE;
		}

		throw new RuntimeException("Invalid contract type: " + contract.getClass().getName());
	}

	protected void setReportEntryHome(ReportEntryHome reportEntryHome) {
		this.reportEntryHome = reportEntryHome;
	}
}
