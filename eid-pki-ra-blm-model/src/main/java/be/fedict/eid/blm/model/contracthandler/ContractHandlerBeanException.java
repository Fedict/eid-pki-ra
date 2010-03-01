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
package be.fedict.eid.blm.model.contracthandler;

import java.util.List;

import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Exception thrown when an error occurred while handling the contract.
 * @author Jan Van den Bergh
 */
public class ContractHandlerBeanException extends Exception {

	private static final long serialVersionUID = -6773414424819266033L;

	private final ResultType resultType;

	private final List<String> messages;
	
	public ContractHandlerBeanException(ResultType resultType, String message) {
		super(message);
		this.resultType =resultType; 
		this.messages = null;
	}
	
	public ContractHandlerBeanException(ResultType resultType, List<String> messages) {
		super(joinMessages(messages));
		this.resultType =resultType; 
		this.messages = messages;
	}

	public ContractHandlerBeanException(ResultType resultType, String message, Throwable t) {
		super(message, t);
		this.resultType = resultType;
		this.messages = null;
	}

	public ResultType getResultType() {
		return resultType;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	private static String joinMessages(List<String> messages) {
        StringBuffer buffer = new StringBuffer();
        for(String message: messages) {
        	if (buffer.length()!=0) {
        		buffer.append(", ");        		
        	}
        	buffer.append(message);
        }        
        return buffer.toString();
    }
}
