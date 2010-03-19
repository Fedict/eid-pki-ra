package be.fedict.eid.pkira.blm.model.handler;

import java.util.List;

import javax.ejb.Local;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;

@Local
public interface RegistrationHandler {
	
	String NAME = "registrationHandler";
	
	List<CertificateDomain> getSelectedCertificateDomains();
	
	void setSelectedCertificateDomains(List<CertificateDomain> selectedCertificateDomains);
	
	void createRegistrations();
	
	String registerUser();
	
	void destroy();
}
