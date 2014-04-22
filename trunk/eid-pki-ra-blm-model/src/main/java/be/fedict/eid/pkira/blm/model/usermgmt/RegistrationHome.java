/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

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
		// raise an event for sending the approval mail
		raiseEvent(RegistrationMailHandler.REGISTRATION_APPROVED, getInstance(), reason);
		return "approved";
	}
	
	public String disapprove(){
		return disapprove(getInstance().getId(), getReason());
	}

	public String disapprove(Integer id, String reason) {
		setId(id);
		remove();
		// raise an event for sending the disapproval mail
		raiseEvent(RegistrationMailHandler.REGISTRATION_DISAPPROVED, getInstance(), reason);
		return "disapproved";
	}
}
