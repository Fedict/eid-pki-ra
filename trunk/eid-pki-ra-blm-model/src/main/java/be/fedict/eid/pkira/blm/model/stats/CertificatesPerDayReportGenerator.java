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
@Name(CertificatesPerDayReportGenerator.NAME)
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CertificatesPerDayReportGenerator extends ReportGeneratorHelper implements StatisticsReportGenerator {

	public static final String NAME = "be.fedict.eid.pkira.blm.certificatesPerDayReportGenerator";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void setQueryParameters(Query query) {
		query.setParameter("result", ResultType.SUCCESS);
	}

	@Override
	protected String getQueryString() {
		return "SELECT year(creationDate),month(creationDate),day(creationDate), COUNT(*)" +
				" FROM CertificateSigningContract" +
				" WHERE result=:result" +
				" GROUP BY year(creationDate),month(creationDate),day(creationDate)" +
				" ORDER BY year(creationDate) DESC,month(creationDate) DESC,day(creationDate) DESC";
	}

	@Override
	protected List<Object> getValuesFromDataRow(Object[] items) {
		return Arrays.asList(getDayFromStartOfDataRow(items), items[3]);
	}

	@Override
	public List<ReportColumn> getReportColumns() {
		return Arrays.asList(new ReportColumn(ReportColumnType.DATE, "stats.date", "dd/MM/yyyy"), new ReportColumn(
				ReportColumnType.LONG, "stats.numberOfCertificates"));
	}
}
