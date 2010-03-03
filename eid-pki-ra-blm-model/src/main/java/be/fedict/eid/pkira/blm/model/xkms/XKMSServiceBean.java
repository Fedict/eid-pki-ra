package be.fedict.eid.pkira.blm.model.xkms;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.ejb.Stateless;

import org.apache.commons.io.FileUtils;
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
		URL resource = getClass().getResource("/aca-it.be.crt");
		File resourceFile = new File(resource.getFile());
		try {
			return FileUtils.readFileToString(resourceFile);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read certificate from file", e);
		}
	}

}
