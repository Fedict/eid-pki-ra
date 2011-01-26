package be.fedict.eid.pkira.dnfilter;

/**
 * Exception thrown when a DN filter exception is invalid.
 * 
 * @author Jan Van den Bergh
 */
public class InvalidDistinguishedNameException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidDistinguishedNameException() {
	}

	public InvalidDistinguishedNameException(String message) {
		super(message);
	}

	public InvalidDistinguishedNameException(Throwable cause) {
		super(cause);
	}

	public InvalidDistinguishedNameException(String message, Throwable cause) {
		super(message, cause);
	}

}
