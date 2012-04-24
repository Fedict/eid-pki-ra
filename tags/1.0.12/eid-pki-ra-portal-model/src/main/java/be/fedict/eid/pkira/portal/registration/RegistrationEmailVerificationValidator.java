package be.fedict.eid.pkira.portal.registration;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.validator.Validator;


public class RegistrationEmailVerificationValidator implements Validator<RegistrationEmailVerification> {

	@Override
	public boolean isValid(Object value) {
		if (value==null) { 
			return true;
		}
		
		if (value instanceof Registration) {
			Registration registration = (Registration) value;
			return ObjectUtils.equals(registration.getEmailAddress(), registration.getEmailAddressVerification());
		}
		
		return false;
	}

	@Override
	public void initialize(RegistrationEmailVerification parameters) {
		// No work here.
	}

}
