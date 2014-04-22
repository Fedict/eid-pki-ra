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

package be.fedict.eid.pkira.blm.model.usermgmt.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Validator;

/**
 * @author Bram Baeyens
 */
public class ValidNationalRegisterNumberValidator implements Validator<ValidNationalRegisterNumber> {

	private static final String NUMBER_REGEX = "\\d{11,11}";
	
	@Override
	public void initialize(ValidNationalRegisterNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value) {
		String nationalRegisterNumber = (String) value;
		if (StringUtils.isEmpty(nationalRegisterNumber)) {
			return true;
		}
		
		return isValidNumberPattern(nationalRegisterNumber);
	}

	private boolean isValidNumberPattern(String nationalRegisterNumber)  {
		Pattern numberPattern = Pattern.compile(NUMBER_REGEX);
		Matcher matcher = numberPattern.matcher(nationalRegisterNumber);    
		
		return matcher.matches();
	}
}
