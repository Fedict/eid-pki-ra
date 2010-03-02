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
package be.fedict.eid.blm.model.validation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.EmailValidator;
import org.hibernate.validator.NotEmptyValidator;
import org.hibernate.validator.NotNullValidator;
import org.jboss.seam.annotations.In;

import be.fedict.eid.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CryptoException;
import be.fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType;
import be.fedict.eid.pkira.generated.contracts.CertificateSigningRequestType;
import be.fedict.eid.pkira.generated.contracts.EntityType;
import be.fedict.eid.pkira.generated.contracts.ResultType;

/**
 * Implementation (bean) of the FieldValidator.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
public class FieldValidatorBean implements FieldValidator {

	private static final String PHONE_PATTERN = "(\\+|0)[-0-9 \\./]+";

	@In(value=CSRParser.NAME)
	private CSRParser csrParser;

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.validation.FieldValidator#validateContract(be
	 * .fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType)
	 */
	@Override
	public void validateContract(CertificateRevocationRequestType contract) throws ContractHandlerBeanException {
		// TODO Add validation logic.
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.validation.FieldValidator#validateContract(be
	 * .fedict.eid.pkira.generated.contracts.CertificateSigningRequestType)
	 */
	@Override
	public void validateContract(CertificateSigningRequestType contract) throws ContractHandlerBeanException {
		// Do some basic field checks
		List<String> messages = new ArrayList<String>();
		validateNotNull("contract", contract, messages);
		if (contract != null) {
			validateCSR(contract.getCSR(), contract.getDistinguishedName(), messages);
			validateNotEmpty("description", contract.getDescription(), messages);
			validateNotEmpty("distinguished name", contract.getDistinguishedName(), messages);
			validateNotEmpty("legal notice", contract.getLegalNotice(), messages);
			validateNotEmpty("request ID", contract.getRequestId(), messages);
			validateNotNull("certificate type", contract.getCertificateType(), messages);
			validateNotNull("signature", contract.getSignature(), messages);
			validateNotNull("validity period in months", contract.getValidityPeriodMonths(), messages);
			validateValidityPeriod(contract.getValidityPeriodMonths(), messages);
			validateOperator(contract.getOperator(), messages);
		}

		if (messages.size() != 0) {
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, messages);
		}

		// TODO Add other validation logic.
	}
	
	protected void validateCSR(String csr, String distinguishedName, List<String> messages) {
		validateNotEmpty("CSR", csr, messages);
		if (StringUtils.isNotEmpty(csr)) {
			try {
				CSRInfo csrInfo = csrParser.parseCSR(csr);
				if (!StringUtils.equals(distinguishedName, csrInfo.getSubject())) {
					messages.add("csr does not match distinguished name");
				}
			} catch (CryptoException e) {
				messages.add("invalid csr: " + e.getMessage());
			}
		}
	}

	protected void validateValidityPeriod(BigInteger validityPeriodMonths, List<String> messages) {
		// TODO use list of validities from database.
		if (validityPeriodMonths != null && validityPeriodMonths.intValue() != 15) {
			messages.add("invalid validity period");
		}
	}

	protected void validateOperator(EntityType operator, List<String> messages) {
		validateNotNull("operator", operator, messages);
		if (operator != null) {
			validateNotEmpty("operator e-mail", operator.getEmail(), messages);
			validateNotEmpty("operator function", operator.getFunction(), messages);
			validateNotEmpty("operator name", operator.getName(), messages);
			validateNotEmpty("operator phone", operator.getPhone(), messages);
			validateEmail("operator e-mail", operator.getEmail(), messages);
			validatePhone("operator phone", operator.getPhone(), messages);
		}
	}

	protected void validatePhone(String name, String value, List<String> messages) {
		if (value != null && !Pattern.matches(PHONE_PATTERN, value)) {
			messages.add(name + " is not a valid phone number");
		}
	}

	protected void validateEmail(String name, String value, List<String> messages) {
		EmailValidator emailValidator = new EmailValidator();
		emailValidator.initialize(null);
		if (value != null && !emailValidator.isValid(value)) {
			messages.add("invalid e-mail value for " + name);
		}
	}

	protected void validateNotNull(String name, Object value, List<String> messages) {
		if (!new NotNullValidator().isValid(value)) {
			messages.add(name + " is missing");
		}
	}

	protected void validateNotEmpty(String name, String value, List<String> messages) {
		if (!new NotEmptyValidator().isValid(value)) {
			messages.add(name + " cannot be empty");
		}
	}

	protected void setCSRParser(CSRParser csrParser) {
		this.csrParser=csrParser;		
	}
}
