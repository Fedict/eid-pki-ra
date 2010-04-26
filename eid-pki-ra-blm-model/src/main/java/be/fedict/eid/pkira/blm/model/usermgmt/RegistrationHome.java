package be.fedict.eid.pkira.blm.model.usermgmt;

import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomainHome;

@Name(RegistrationHome.NAME)
public class RegistrationHome extends EntityHome<Registration> {

	private static final long serialVersionUID = 8487479089538426398L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.registrationHome";
	
	@In(value = CertificateDomainHome.NAME, create=true)
	private CertificateDomainHome certificateDomainHome;
	
	private String reason;

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
	
	@Override
	public String getDeletedMessageKey() {
		return "registration.deleted";
	}

	public String approve() {
		return approve(getInstance().getId(), getReason());
	}

	public String approve(Integer id, String reason) {
		setId(id);
		getInstance().setStatus(RegistrationStatus.APPROVED);
		update();
		raiseEvent("be.fedict.eid.pkira.blm.registrationHome.approved", getInstance(), reason);
		return "approved";
	}
	
	public String disapprove(){
		return disapprove(getInstance().getId(), getReason());
	}

	public String disapprove(Integer id, String reason) {
		setId(id);
		remove();
		raiseEvent("be.fedict.eid.pkira.blm.registrationHome.disapproved", getInstance(), reason);
		return "disapproved";
	}
}
