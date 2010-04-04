package be.fedict.eid.pkira.blm.model.certificatedomain;

/**
 * Exception thrown to indicate that a DN overlaps with an already existing one.
 * 
 * @author Jan Van den Bergh
 */
public class DistinguishedNameOverlapsException extends CertificateDomainException {

	private static final long serialVersionUID = -889653983559501133L;

	public DistinguishedNameOverlapsException() {
		super("certificatedomain.error.dnoverlaps");
	}

}
