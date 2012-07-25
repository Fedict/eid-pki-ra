/**
 * eID PKI RA Project. 
 * Copyright (C) 2010-2012 FedICT. 
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
package be.fedict.eid.pkira.blm.model.contracts;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

@Name(ContractListHandler.NAME)
@Scope(ScopeType.CONVERSATION)
public class ContractListHandlerBean implements ContractListHandler {

	private static final long serialVersionUID = -5017092109042531175L;

	private static final String CONTRACTS_NAME = "contracts";

	@DataModel
	private List<AbstractContract> contracts;

	private Integer certificateDomainId;

	@In(value = ContractQuery.NAME, create = true)
	private ContractQuery contractQuery;

	@Override
	public List<AbstractContract> findContractList() {
		contracts = contractQuery.getFindContracts(certificateDomainId);
		return contracts;
	}

	@Factory(CONTRACTS_NAME)
	public void initCertificateList() {
		findContractList();
	}

	public void setCertificateDomainId(Integer certificateDomainId) {
		this.certificateDomainId = certificateDomainId;
	}

	public Integer getCertificateDomainId() {
		return certificateDomainId;
	}

}
