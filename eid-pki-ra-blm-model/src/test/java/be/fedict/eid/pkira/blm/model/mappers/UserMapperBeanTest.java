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
package be.fedict.eid.pkira.blm.model.mappers;

import org.testng.annotations.Test;

import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.generated.privatews.UserWS;

import static org.testng.Assert.*;

/**
 * @author Jan Van den Bergh
 */
public class UserMapperBeanTest {

	private static final String TEST_FIRSTNAME = "first";
	private static final Integer TEST_ID = 42;
	private static final String TEST_LASTNAME = "last";
	private static final String TEST_NRN = "nrn";

	private UserMapperBean bean = new UserMapperBean();

	@Test
	public void testMapUser() {
		UserWS userWS = bean.map(createUser(), true);

		assertNotNull(userWS);
		validateUserWS(userWS);
	}

	@Test
	public void testMapUserNull() {
		assertNull(bean.map(null, true));
	}

	private void validateUserWS(UserWS userWS) {
		assertEquals(userWS.getId(), TEST_ID.toString());
		assertEquals(userWS.getFirstName(), TEST_FIRSTNAME);
		assertEquals(userWS.getLastName(), TEST_LASTNAME);
		assertEquals(userWS.getNationalRegisterNumber(), TEST_NRN);
		assertTrue(userWS.isWithRegistrations());
	}

	private User createUser() {
		User result = new User();
		result.setId(TEST_ID);
		result.setFirstName(TEST_FIRSTNAME);
		result.setLastName(TEST_LASTNAME);
		result.setNationalRegisterNumber(TEST_NRN);

		return result;
	}

}
