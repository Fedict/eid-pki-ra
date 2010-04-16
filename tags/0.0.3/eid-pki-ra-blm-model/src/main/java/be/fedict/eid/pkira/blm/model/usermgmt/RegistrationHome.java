package be.fedict.eid.pkira.blm.model.usermgmt;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;

@Name("registrationHome")
public class RegistrationHome extends EntityHome<Registration> {

	private static final long serialVersionUID = 8487479089538426398L;
	
	@In(value=RegistrationManager.NAME, create=true)
	private RegistrationManager registrationManager;
	
	@In(value = CertificateDomainHome.NAME, create=true)
	private CertificateDomainHome certificateDomainHome;
	
	private String reason;

	@Override
	@Factory(value="registration", scope=ScopeType.EVENT)
	public Registration getInstance(){
		return super.getInstance();
	}
	
	public String approve() {
		registrationManager.approveRegistration(getInstance().getId(),this.reason);
		return "approved";
	}
	
	public String disapprove(){
		registrationManager.disapproveRegistration(getInstance().getId(),this.reason);
		return "disapproved";
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public List<CertificateDomain> getCertificateDomains() {
		List<CertificateDomain> certificateDomains = certificateDomainHome.findUnregistered(getInstance().getRequester());
		certificateDomains.add(getInstance().getCertificateDomain());
		return certificateDomains;
	}
}
