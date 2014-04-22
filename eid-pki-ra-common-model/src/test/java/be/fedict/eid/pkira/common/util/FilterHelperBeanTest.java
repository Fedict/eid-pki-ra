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

package be.fedict.eid.pkira.common.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class FilterHelperBeanTest {

	private final FilterHelper filterHelper = new FilterHelperBean();
	
	@Test
	public void matchRegularExpression() {
		assertTrue(filterHelper.matchRegularExpression("a+", "aaa"));
		assertFalse(filterHelper.matchRegularExpression("aa+", "a"));
	}

	@Test
	public void matchWildcardExpression() {
		assertTrue(filterHelper.matchWildcardExpression("[a]*", "[a]ba"));
		assertFalse(filterHelper.matchWildcardExpression("a*?", "a"));
	}

}
