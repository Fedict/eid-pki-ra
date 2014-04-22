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

package be.fedict.eid.pkira.blm.model.stats;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Report of the number of certificates per day.
 * 
 * @author jan
 */
@Name(CertificatesPerCertificateDomainPerDayReportGenerator.NAME)
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CertificatesPerCertificateDomainPerDayReportGenerator extends ReportGeneratorHelper implements StatisticsReportGenerator {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificatesPerCertificateDomainPerDayReportGenerator";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected String getQueryString() {
		return "SELECT year(c.creationDate),month(c.creationDate),day(c.creationDate),c.certificateDomain.name,str(c.certificateDomain.dnExpression), COUNT(*)" +
				" FROM CertificateSigningContract c" +
				" WHERE c.result=:result" +
				" GROUP BY year(c.creationDate),month(creationDate),day(c.creationDate),c.certificateDomain.name, str(c.certificateDomain.dnExpression)" +
				" ORDER BY year(c.creationDate) DESC,month(creationDate) DESC,day(c.creationDate) DESC,c.certificateDomain.name ASC";
	}

	@Override
	protected void setQueryParameters(Query query) {
		query.setParameter("result", ResultType.SUCCESS);

	}

	@Override
	protected List<Object> getValuesFromDataRow(Object[] items) {
		return Arrays.asList(getDayFromStartOfDataRow(items), items[3], items[4], items[5]);
	}

	@Override
	public List<ReportColumn> getReportColumns() {
		return Arrays.asList(
			new ReportColumn(ReportColumnType.DATE, "stats.date", "dd/MM/yyyy"),
			new ReportColumn(ReportColumnType.LABEL, "stats.certificateDomainName"),
			new ReportColumn(ReportColumnType.LABEL, "stats.certificateDomainExpression"),
			new ReportColumn(ReportColumnType.LONG, "stats.numberOfCertificates")
		);			
	}
	
}
