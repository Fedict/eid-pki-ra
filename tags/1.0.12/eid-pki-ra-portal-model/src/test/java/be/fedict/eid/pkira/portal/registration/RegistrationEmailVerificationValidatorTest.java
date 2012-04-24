package be.fedict.eid.pkira.portal.registration;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

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
