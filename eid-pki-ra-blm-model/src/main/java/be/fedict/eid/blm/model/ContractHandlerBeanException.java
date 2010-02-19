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
