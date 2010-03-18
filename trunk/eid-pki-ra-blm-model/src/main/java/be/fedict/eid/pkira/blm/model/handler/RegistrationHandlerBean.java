package be.fedict.eid.pkira.blm.model.handler;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainRepository;
import be.fedict.eid.pkira.blm.model.domain.Registration;
import be.fedict.eid.pkira.blm.model.domain.RegistrationStatus;
import be.fedict.eid.pkira.blm.model.domain.User;
import be.fedict.eid.pkira.blm.model.jpa.RegistrationRepository;
import be.fedict.eid.pkira.blm.model.jpa.UserRepository;

@Stateful
@Name(RegistrationHandler.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Scope(ScopeType.CONVERSATION)
public class RegistrationHandlerBean implements RegistrationHandler, Serializable {

	private static final long serialVersionUID = 1816007018506139983L;
	
	@In(value="newRegistration")
	private Registration registration;
	private List<CertificateDomain> selectedCertificateDomains;
	@In(create=true, value=RegistrationRepository.NAME)
	private RegistrationRepository registrationRepository;
	@In(create=true, value=UserRepository.NAME)
	private UserRepository userRepository;
	@In(create=true, value=CertificateDomainRepository.NAME)
	private CertificateDomainRepository certificateDomainRepository;

	@Override
	@Remove
	public void createRegistrations() {
		User requester = userRepository.getReference(registration.getRequester().getId());
		for (CertificateDomain certificateDomain : selectedCertificateDomains) {
			Registration newRegistration = new Registration();
			newRegistration.setRequester(requester);
			newRegistration.setEmail(registration.getEmail());
			newRegistration.setStatus(RegistrationStatus.NEW);
			newRegistration.setCertificateDomain(
					certificateDomainRepository.getReference(certificateDomain.getId()));
			registrationRepository.persist(newRegistration);
		}
	}

	@Override
	@Begin(join=true)
	public List<CertificateDomain> getSelectedCertificateDomains() {
		return selectedCertificateDomains;
	}

	@Override
	public void setSelectedCertificateDomains(List<CertificateDomain> selectedCertificateDomains) {
		this.selectedCertificateDomains = selectedCertificateDomains;
	}
	
	protected void setRegistration(Registration registration) {
		this.registration = registration;
	}
	
	protected void setRegistrationRepository(RegistrationRepository registrationRepository) {
		this.registrationRepository = registrationRepository;
	}
	
	protected void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	protected void setCertificateDomainRepository(CertificateDomainRepository certificateDomainRepository) {
		this.certificateDomainRepository = certificateDomainRepository;
	}
}
