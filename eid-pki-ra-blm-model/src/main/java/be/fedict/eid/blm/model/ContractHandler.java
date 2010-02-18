package be.fedict.eid.blm.model;

import javax.ejb.Local;

@Local
public interface ContractHandler {

	/**
	 * Handles a certificate signing request.
	 * @param request request to sign: XML document, base-64 encoded.
	 * @return response message: XML document, base-64 encoded.
	 */
	public abstract String signCertificate(String request);

	/**
	 * Handles a certificate revocation request.
	 * @param request request to sign: XML document, base-64 encoded.
	 * @return response message: XML document, base-64 encoded.
	 */
	public abstract String revokeCertificate(String request);

}