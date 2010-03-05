package be.fedict.eid.pkira.blm.model.xkms;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

/**
 * XKMS Service implementation.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(XKMSService.NAME)
public class XKMSServiceBean implements XKMSService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean revoke(X509Certificate certificate) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sign(String csr) {
		try {
			InputStream stream = getClass().getResourceAsStream("/aca-it.be.crt");
			
			byte[] bytes = new  byte[512];
			int bytesRead;
			StringBuffer result = new StringBuffer();
			while (-1 != (bytesRead=stream.read(bytes))) {
				result.append(new String(bytes, 0, bytesRead));
			}
			
			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException("Cannot read certificate from file", e);
		}
	}

}
