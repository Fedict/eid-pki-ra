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
package be.fedict.eid.pkira.portal.contract;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.portal.framework.DataTableWSQuery;

/**
 * @author Bram Baeyens
 *
 */
@Name(ContractWSQuery.NAME)
@Scope(ScopeType.CONVERSATION)
public class ContractWSQuery extends DataTableWSQuery {

	private static final long serialVersionUID = 3439133481861208655L;
	
	public static final String NAME = "be.fedict.eid.pkira.portal.contractWSQuery";

    @In(value=ContractDataModel.NAME, create=true)
    private ContractDataModel contractDataModel;

	public ContractDataModel getDataModel() {
		return contractDataModel;
	}

}
