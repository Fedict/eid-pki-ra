package be.fedict.eid.pkira.portal.certificate;

import java.util.List;

import javax.ejb.Local;

@Local
public interface CertificateList {
	
	String NAME = "certificatelist";
	
	public List<Certificate> findCertificateList();
	
	public void certificateList();
	
	public void destroy();
}
