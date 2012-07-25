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

package be.fedict.eid.pkira.blm.model.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * @author Bram Baeyens
 *
 */
public enum ConfigurationEntryType {
	
	OPTIONAL_STRING {
		@Override
		public boolean isValid(String value) {
			return value!=null;
		}
	},
	
	STRING {
		@Override
		public boolean isValid(String value) {
			return StringUtils.isNotBlank(value);
		}
	}, 
	
	DIGITS {
		@Override
		public boolean isValid(String value) {
			return NumberUtils.isDigits(value);
		}
	}, 
	
	NUMBER {
		@Override
		public boolean isValid(String value) {
			return NumberUtils.isNumber(value);
		}
	}, 
	
	URL {
		@Override
		public boolean isValid(String value) {
			if (StringUtils.isBlank(value)) {
				return false;
			}
			try {
				new URL(value);
				return true;
			} catch (MalformedURLException e) {
				return false;
			}
		}
	}, 
	
	COMMA_SEPARATED_DIGITS {
		@Override
		public boolean isValid(String value) {
			if (StringUtils.isBlank(value)) {
				return false;
			}
			String[] values = value.split(",");
			for (String number : values) {
				if (! NumberUtils.isDigits(number)) {
					return false;
				}
			}
			return true;
		}
	}; 
	
	public abstract boolean isValid(String value);
}
