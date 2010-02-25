package be.fedict.eid.blm.model.xkms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.ejb.Stateless;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Stateless
public class XKMSServiceBean implements XKMSService {

	@Override
	public boolean revoke(X509Certificate certificate) {
		return true;
	}

	@Override
	public X509Certificate sign() {
		//TODO: 
		String file = "../eid-pki-ra-blm-model/src/main/resources/aca-it.be.crt";
		//TODO:
		String type = "X.509";

		try {
			FileInputStream fis = new FileInputStream(file);
			CertificateFactory cf = CertificateFactory.getInstance(type, new BouncyCastleProvider());
	
			return (X509Certificate) cf.generateCertificate(fis);		
		} catch (FileNotFoundException e) {
			//TODO: 
			throw new RuntimeException(e);
		} catch (CertificateException e) {
			//TODO:
			throw new RuntimeException(e);
		}
		
	}
	
}
