package be.fedict.eid.pkira.blm.model.handler;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainRepository;

import be.fedict.eid.pkira.authentication.AuthenticationException;
import be.fedict.eid.pkira.authentication.EIdUser;
import be.fedict.eid.pkira.blm.model.usermgmt.Registration;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationRepository;
import be.fedict.eid.pkira.blm.model.usermgmt.RegistrationStatus;
import be.fedict.eid.pkira.blm.model.usermgmt.User;
import be.fedict.eid.pkira.blm.model.usermgmt.UserRepository;

@Stateful
@Name(RegistrationHandler.NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Scope(ScopeType.CONVERSATION)
public class RegistrationHandlerBean implements RegistrationHandler, Serializable {

	private static final long serialVersionUID = 1816007018506139983L;
	
	@In(required=false, value="newRegistration")
	@Out(value="newRegistration")
	private Registration registration;
	private List<CertificateDomain> selectedCertificateDomains;
	@In(create=true, value=RegistrationRepository.NAME)
	private RegistrationRepository registrationRepository;
	@In(create=true, value=UserRepository.NAME)
	private UserRepository userRepository;
	@In(create=true, value=CertificateDomainRepository.NAME)
	private CertificateDomainRepository certificateDomainRepository;
	@In(create=true, value=AuthenticationHandler.NAME)
	private AuthenticationHandler authenticationHandler;
	@In
	private FacesMessages facesMessages;
	@In(required=false, scope=ScopeType.SESSION)
	@Out(required=false, scope=ScopeType.SESSION)
	private User currentUser;

	@Override
	@End
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
	public String registerUser() {
		if (currentUser == null) {
			try {
				EIdUser eiIdUser = authenticationHandler.getUserCredentials();	
				if (eiIdUser == null) {
					facesMessages.addFromResourceBundle("validation.invalid.user");
					return null;
				}
				currentUser = userRepository.findByNationalRegisterNumber(eiIdUser.getIdentifier());
				if (currentUser == null) {
					currentUser = new User();
					currentUser.setNationalRegisterNumber(eiIdUser.getIdentifier());
					currentUser.setLastName(eiIdUser.getLastName());
					currentUser.setFirstName(eiIdUser.getFirstName());
					userRepository.persist(currentUser);
				}
			} catch (AuthenticationException e) {
				facesMessages.addFromResourceBundle("validation.invalid.user");
				return null;
			}
		}
		registration = new Registration();
		registration.setRequester(currentUser);
		return "success";
	}
	
	@Remove
	public void destroy() {
		
	}

	@Override
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
