package be.fedict.eid.pkira.blm.model.certificatedomain;

/**
 * Exception thrown when a certificate domain does not contain any certificate
 * types.
 * 
 * @author Jan Van den Bergh
 */
public class NoCertificateTypesSelectedException extends CertificateDomainException {

	private static final long serialVersionUID = -8229995663883490498L;

	public NoCertificateTypesSelectedException() {
		super("certificatedomain.error.nocertificatetypes");
	}

}
