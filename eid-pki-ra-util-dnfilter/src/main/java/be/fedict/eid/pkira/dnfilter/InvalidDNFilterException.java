package be.fedict.eid.pkira.dnfilter;

/**
 * Exception thrown when a DN filter exception is invalid.
 * 
 * @author Jan Van den Bergh
 */
public class InvalidDNFilterException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidDNFilterException() {
	}

	public InvalidDNFilterException(String message) {
		super(message);
	}

	public InvalidDNFilterException(Throwable cause) {
		super(cause);
	}

	public InvalidDNFilterException(String message, Throwable cause) {
		super(message, cause);
	}

}
