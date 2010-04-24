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

import be.fedict.eid.pkira.blm.model.reporting.ReportEntry.ContractType;

public class AggregateData {

	private String name;
	private ContractType contractType;
	private boolean success;
	private int count;
	
	public AggregateData(String name, ContractType contractType, Boolean success, Long count) {
		this.name = name;
		this.contractType = contractType;
		this.success = success;
		this.count = count.intValue();
	}

	public String getName() {
		return name;
	}
	
	public ContractType getContractType() {
		return contractType;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public int getCount() {
		return count;
	}
}