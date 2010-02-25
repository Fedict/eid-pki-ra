package be.fedict.eid.blm.model.xkms;

import java.security.cert.X509Certificate;

import javax.ejb.Local;

@Local
public interface XKMSService {
	
	/**
	 * sign
	 */
	public X509Certificate sign();
	
	/**
	 * revoke
	 */
	public boolean revoke(X509Certificate certificate);
}
