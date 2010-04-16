package be.fedict.eid.pkira.blm.model.usermgmt;

/**
 * Exception thrown when a registration error occurs.
 * 
 * @author Jan Van den Bergh
 */
public class RegistrationException extends Exception {

	private static final long serialVersionUID = 1L;

	public RegistrationException() {
	}

	public RegistrationException(String message) {
		super(message);
	}

	public RegistrationException(Throwable cause) {
		super(cause);
	}

	public RegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

}
