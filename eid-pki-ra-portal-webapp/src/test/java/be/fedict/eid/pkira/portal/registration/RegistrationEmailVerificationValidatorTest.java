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

package be.fedict.eid.pkira.portal.registration;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RegistrationEmailVerificationValidatorTest {

	@Test
	public void testValidWhenEqual() {
		checkIfValid("test@example.com", "test@example.com", true);
	}

	@Test
	public void testInvalidWhenNotEqual() {
		checkIfValid("test@example.com", "test2@example.com", false);
	}

	@Test
	public void testValidWhenBothNull() {
		checkIfValid(null, null, true);
	}

	@Test
	public void testInvalidWhenOneNull() {
		checkIfValid(null, "test@example.com", false);
	}
	
	@Test
	public void testValidWhenNull() {
		assertTrue(new RegistrationEmailVerificationValidator().isValid(null));
	}
	
	@Test
	public void testInvalidWhenInvalidType() {
		assertFalse(new RegistrationEmailVerificationValidator().isValid("email"));
	}

	protected void checkIfValid(String emailAddress, String emailAddressVerification, boolean valid) {
		Registration registration = new Registration();
		registration.setEmailAddress(emailAddress);
		registration.setEmailAddressVerification(emailAddressVerification);

		if (valid) {
			assertTrue(new RegistrationEmailVerificationValidator().isValid(registration));
		} else {
			assertFalse(new RegistrationEmailVerificationValidator().isValid(registration));
		}
	}
}
