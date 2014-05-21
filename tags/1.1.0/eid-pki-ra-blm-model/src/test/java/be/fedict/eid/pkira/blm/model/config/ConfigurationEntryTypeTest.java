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

package be.fedict.eid.pkira.blm.model.config;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Bram Baeyens
 *
 */
public class ConfigurationEntryTypeTest {
	
	@Test
	public void testString() {
		Assert.assertTrue(ConfigurationEntryType.STRING.isValid("abc"));
	}
	
	@Test
	public void testNullString() {
		Assert.assertFalse(ConfigurationEntryType.STRING.isValid(null));
	}
	
	@Test
	public void testEmptyString() {
		Assert.assertFalse(ConfigurationEntryType.STRING.isValid(""));
	}
	
	@Test
	public void testBlankString() {
		Assert.assertFalse(ConfigurationEntryType.STRING.isValid("  "));
	}
	
	@Test
	public void testDigits() {
		Assert.assertTrue(ConfigurationEntryType.DIGITS.isValid("123"));
	}
	
	@Test
	public void testDigitsWithComma() {
		Assert.assertFalse(ConfigurationEntryType.DIGITS.isValid("123,65"));
	}
	
	@Test
	public void testDigitsWithString() {
		Assert.assertFalse(ConfigurationEntryType.DIGITS.isValid("1ax45"));
	}
	
	@Test
	public void testDigitsHexaDecimal() {
		Assert.assertFalse(ConfigurationEntryType.DIGITS.isValid("1a4b"));
	}
	
	@Test
	public void testDigitsNull() {
		Assert.assertFalse(ConfigurationEntryType.DIGITS.isValid(null));
	}
	
	@Test
	public void testDigitsEmpty() {
		Assert.assertFalse(ConfigurationEntryType.DIGITS.isValid(""));
	}
	
	@Test
	public void testCommaSeparatedDigits() {
		Assert.assertTrue(ConfigurationEntryType.COMMA_SEPARATED_DIGITS.isValid("1,5,10"));
	}
	
	@Test
	public void testCommaSeparatedDigitsWithString() {
		Assert.assertFalse(ConfigurationEntryType.COMMA_SEPARATED_DIGITS.isValid("1,x,10"));
	}
	
	@Test
	public void testCommaSeparatedDigitsWithHexaString() {
		Assert.assertFalse(ConfigurationEntryType.COMMA_SEPARATED_DIGITS.isValid("1,1b,10"));
	}
	
	@Test
	public void testCommaSeparatedDigitsNull() {
		Assert.assertFalse(ConfigurationEntryType.COMMA_SEPARATED_DIGITS.isValid(null));
	}
	
	@Test
	public void testCommaSeparatedDigitsEmpty() {
		Assert.assertFalse(ConfigurationEntryType.COMMA_SEPARATED_DIGITS.isValid(""));
	}
	
	@Test
	public void testURL() {
		Assert.assertTrue(ConfigurationEntryType.URL.isValid("http://www.aca-it.be"));
	}
	
	@Test
	public void testURLNull() {
		Assert.assertFalse(ConfigurationEntryType.URL.isValid(null));
	}
	
	@Test
	public void testURLEmpty() {
		Assert.assertFalse(ConfigurationEntryType.URL.isValid(""));
	}
	
	@Test
	public void testURLInvalid() {
		Assert.assertFalse(ConfigurationEntryType.URL.isValid("invalid"));
	}
	
}
