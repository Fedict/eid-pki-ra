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
package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.NotEmptyValidator;
import org.hibernate.validator.NotNullValidator;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;
import be.fedict.eid.pkira.crypto.CSRInfo;
import be.fedict.eid.pkira.crypto.CSRParser;
import be.fedict.eid.pkira.crypto.CertificateInfo;
import be.fedict.eid.pkira.crypto.CertificateParser;
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
@Name(FieldValidator.NAME)
public class FieldValidatorBean implements FieldValidator {

	private static final String PHONE_PATTERN = "(\\+|0)[-0-9 \\./]+";

	@In(value = CSRParser.NAME, create = true)
	private CSRParser csrParser;
	
	@In(value = CertificateParser.NAME, create=true)
	private CertificateParser certificateParser;

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.model.validation.FieldValidator#validateContract(be
	 * .fedict.eid.pkira.generated.contracts.CertificateRevocationRequestType)
	 */
	@Override
	public void validateContract(CertificateRevocationRequestType contract) throws ContractHandlerBeanException {
		// Do some basic field checks
		List<String> messages = new ArrayList<String>();
		validateNotNull("contract", contract, messages);
		if (contract != null) {
			validateNotEmpty("request id", contract.getRequestId(), messages);
			validateNotEmpty("distinguished name", contract.getDistinguishedName(), messages);
			validateNotNull("start date", contract.getValidityStart(), messages);
			validateNotNull("end date", contract.getValidityEnd(), messages);
			validateNotEmpty("description", contract.getDescription(), messages);
			validateNotEmpty("legal notice", contract.getLegalNotice(), messages);			
			validateNotNull("signature", contract.getSignature(), messages);
			validateOperator(contract.getOperator(), messages);			
			validateCertificate(contract.getCertificate(), contract.getDistinguishedName(), contract.getValidityStart(), contract.getValidityEnd(), messages);						
		}

		if (messages.size() != 0) {
			throw new ContractHandlerBeanException(ResultType.INVALID_MESSAGE, messages);
		}
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
	}

	protected void setCSRParser(CSRParser csrParser) {
		this.csrParser = csrParser;
	}
	
	protected void setCertificateParser(CertificateParser certificateParser) {
		this.certificateParser = certificateParser;
	}

	protected void validateCertificate(String certificate, String distinguishedName, XMLGregorianCalendar startDate,
			XMLGregorianCalendar endDate, List<String> messages) {
		validateNotEmpty("certificate", certificate, messages);
		if (StringUtils.isNotEmpty(certificate)){
			try {
				CertificateInfo certificateInfo = certificateParser.parseCertificate(certificate);
				
				if (!StringUtils.equals(distinguishedName, certificateInfo.getDistinguishedName())) {
					messages.add("distinguished name does not match certificate");
				}
				
				if (startDate!=null && startDate.toGregorianCalendar().getTime().getTime()!=certificateInfo.getValidityStart().getTime()) {
					messages.add("start date does not match certificate");
				}
				
				if (endDate!=null && endDate.toGregorianCalendar().getTime().getTime()!=certificateInfo.getValidityEnd().getTime()) {
					messages.add("end date does not match certificate");
				}
			} catch (CryptoException e) {
				messages.add("invalid certificate: " + e.getMessage());
			}
		}		
	}

	protected void validateCSR(String csr, String distinguishedName, List<String> messages) {
		validateNotEmpty("CSR", csr, messages);
		if (StringUtils.isNotEmpty(csr)) {
			try {
				CSRInfo csrInfo = csrParser.parseCSR(csr);
				if (!StringUtils.equals(distinguishedName, csrInfo.getSubject())) {
					messages.add("distinguished name does not match csr");
				}
			} catch (CryptoException e) {
				messages.add("invalid csr: " + e.getMessage());
			}
		}
	}

	protected void validateNotEmpty(String name, String value, List<String> messages) {
		if (!new NotEmptyValidator().isValid(value)) {
			messages.add(name + " cannot be empty");
		}
	}

	protected void validateNotNull(String name, Object value, List<String> messages) {
		if (!new NotNullValidator().isValid(value)) {
			messages.add(name + " is missing");
		}
	}

	protected void validateOperator(EntityType operator, List<String> messages) {
		validateNotNull("operator", operator, messages);
		if (operator != null) {
			validateNotEmpty("operator function", operator.getFunction(), messages);
			validateNotEmpty("operator name", operator.getName(), messages);
			validateNotEmpty("operator phone", operator.getPhone(), messages);
			validatePhone("operator phone", operator.getPhone(), messages);
		}
	}

	protected void validatePhone(String name, String value, List<String> messages) {
		if (value != null && !Pattern.matches(PHONE_PATTERN, value)) {
			messages.add(name + " is not a valid phone number");
		}
	}

	protected void validateValidityPeriod(BigInteger validityPeriodMonths, List<String> messages) {
		// TODO use list of validities from database.
		if (validityPeriodMonths != null && validityPeriodMonths.intValue() != 15) {
			messages.add("invalid validity period");
		}
	}
}
