/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.portal.registration;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;

import be.fedict.eid.pkira.authentication.EIdUser;
import be.fedict.eid.pkira.common.security.EIdUserCredentials;
import be.fedict.eid.pkira.generated.privatews.CertificateDomainWS;
import be.fedict.eid.pkira.privatews.EIDPKIRAPrivateServiceClient;

/**
 * @author Jan Van den Bergh
 */
@Name(RegistrationHandler.NAME)
@Scope(ScopeType.EVENT)
public class RegistrationHandler {

	public static final String NAME = "be.fedict.pkira.portal.registrationHandler";
	public static final String REMAINING_CERTIFICATE_DOMAINS = "be.fedict.pkira.portal.remainingCertificateDomains";
	public static final String CERTIFICATE_DOMAIN_NAME = "be.fedict.pkira.portal.certificateDomainName";

	@In(value = EIDPKIRAPrivateServiceClient.NAME, create = true)
	private EIDPKIRAPrivateServiceClient serviceClient;

	@In
	private EIdUserCredentials credentials;

	@In(value = Registration.NAME, create = true)
	private Registration registration;

	@In
	private FacesMessages facesMessages;

	/**
	 * {@inheritDoc}
	 */
	@Begin(join = true)
	@Factory(value = RegistrationHandler.REMAINING_CERTIFICATE_DOMAINS, scope = ScopeType.CONVERSATION)
	public List<CertificateDomainWS> getCertificateDomains() {
		String userRRN = credentials.getUsername();
		return serviceClient.findRemainingCertificateDomainsForUser(userRRN);
	}

	/**
	 * {@inheritDoc}
	 */
	public String submitRegistration() {
		EIdUser user = credentials.getUser();
		boolean result = serviceClient.createRegistrationForUser(user.getRRN(), user.getLastName(),
				user.getFirstName(), registration.getSelectedDomainId(), registration.getEmailAddress());

		if (result) {
			facesMessages.addFromResourceBundle("registration.success");
			Conversation.instance().end();
			return "success";
		} else {
			facesMessages.addFromResourceBundle("registration.error");
			return null;
		}
	}
}
