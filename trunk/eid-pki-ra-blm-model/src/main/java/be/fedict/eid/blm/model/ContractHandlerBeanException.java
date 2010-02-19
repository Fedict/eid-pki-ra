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
package be.fedict.eid.blm.model;

import be.fedict.eid.pkira.contracts.ResultType;

/**
 * Exception thrown when an error occurred while handling the contract.
 * @author Jan Van den Bergh
 */
public class ContractHandlerBeanException extends Exception {

	private static final long serialVersionUID = -6773414424819266033L;

	private final ResultType resultType;
	
	public ContractHandlerBeanException(ResultType resultType, String message) {
		super(message);
		this.resultType =resultType; 
	}
	
	public ContractHandlerBeanException(ResultType resultType, String message, Throwable t) {
		super(message, t);
		this.resultType = resultType;
	}

	public ResultType getResultType() {
		return resultType;
	}
}
