package be.fedict.eid.pkira.blm.model.xkms;

import java.security.cert.X509Certificate;

import javax.ejb.Local;

@Local
public interface XKMSService {
	
	/**
	 * Let the CA sign the CSR. If this fails, the error will be logged here.
	 * @param csr The CSR to sign in PEM format.
	 * @return the signed certificate in PEM format. This is null if a problem occurred.
	 */
	public String sign(String csr);
	
	/**
	 * revoke
	 */
	public boolean revoke(X509Certificate certificate);
}
