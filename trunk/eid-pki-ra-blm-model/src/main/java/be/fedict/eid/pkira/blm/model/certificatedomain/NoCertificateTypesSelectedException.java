package be.fedict.eid.pkira.blm.model.certificatedomain;

/**
 * Exception thrown when a certificate domain does not contain any certificate
 * types.
 * 
 * @author Jan Van den Bergh
 */
public class NoCertificateTypesSelectedException extends Exception {

	private static final long serialVersionUID = -8229995663883490498L;

	public NoCertificateTypesSelectedException() {
	}

	public NoCertificateTypesSelectedException(String message) {
		super(message);
	}

	public NoCertificateTypesSelectedException(Throwable cause) {
		super(cause);
	}

	public NoCertificateTypesSelectedException(String message, Throwable cause) {
		super(message, cause);
	}

}
