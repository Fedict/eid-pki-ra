package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.model.contracthandler.ContractHandlerBeanException;

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
	public void revoke(String certificate) throws ContractHandlerBeanException {
		//TODO implement me when we have an XKMS service
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String sign(String csr) throws ContractHandlerBeanException {
		//TODO implement me when we have an XKMS service
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
