package be.fedict.eid.pkira.blm.model.certificatedomain;

/**
 * Exception thrown to indicate that a DN overlaps with an already existing one.
 * 
 * @author Jan Van den Bergh
 */
public class DistinguishedNameOverlapsException extends Exception {

	private static final long serialVersionUID = 1L;

	public DistinguishedNameOverlapsException() {		
	}

	public DistinguishedNameOverlapsException(String message) {
		super(message);
	}

	public DistinguishedNameOverlapsException(Throwable cause) {
		super(cause);
	}

	public DistinguishedNameOverlapsException(String message, Throwable cause) {
		super(message, cause);
	}

}
