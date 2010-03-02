package be.fedict.eid.blm.model.certificatehandler;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import be.fedict.eid.blm.model.jpa.Certificate;

@Local
public interface CertificateHandler {
	/**
	 * 
	 * @return
	 */
	public List<Certificate> findAllCertificates();
	
	/**
	 * 
	 * @return
	 */
	public List<Certificate> findCertificate(); 
	
	
	/**
 	* 
 	*/
	public void createCertificate(String x509, String subject, Date validityStart,
			Date validityEnd, String requestorName, String issuer);
	
	/**
	 * 
	 */
	public void deleteCertificate();
}
