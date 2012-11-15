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
package be.fedict.eid.pkira.blm.model.reporting;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

/**
 * Query to get the list of months.
 * 
 * @author Jan Van den Bergh
 */
@Name(ReportMonthQuery.NAME)
@Scope(ScopeType.CONVERSATION)
public class ReportMonthQuery extends EntityQuery<String> {

	public static final String NAME = "be.fedict.eid.pkira.blm.reportMonthQuery";

	private static final long serialVersionUID = 1171619330822348557L;

	@Override
	public String getEjbql() {
		return "SELECT DISTINCT month FROM ReportEntry";
	}

	@Override
	public String getOrderColumn() {
		return "month";
	}

	@Override
	public String getOrderDirection() {
		return "DESC";
	}

}
