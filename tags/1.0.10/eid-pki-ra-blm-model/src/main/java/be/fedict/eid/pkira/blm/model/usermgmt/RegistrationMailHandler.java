/**
 * eID PKI RA Project. 
 * Copyright (C) 2010 FedICT. 
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;

import be.fedict.eid.pkira.blm.model.mail.MailTemplate;

/**
 * @author Bram Baeyens
 * 
 */
@Name(RegistrationMailHandler.NAME)
public class RegistrationMailHandler implements Serializable {

	public static final String REGISTRATION_DISAPPROVED = "be.fedict.eid.pkira.blm.registrationHome.disapproved";

	public static final String REGISTRATION_APPROVED = "be.fedict.eid.pkira.blm.registrationHome.approved";

	private static final long serialVersionUID = -6295254696596001136L;
	
	public static final String NAME = "be.fedict.eid.pkira.blm.registrationMailHandler";

	public static final String REGISTRATION_CREATED = "be.fedict.eid.pkira.blm.registration.created";
	
	@In(value = MailTemplate.NAME, create = true)
	private MailTemplate mailTemplate;
	
	@In(value=UserRepository.NAME, create=true)
	private UserRepository userRepository;
	
	@Observer(REGISTRATION_CREATED)
	public void sendCreated(Registration registration) {
		Map<String, Object> parameters = createMapForRegistrationMail(registration, null);
		for(User user: userRepository.getAdminUsersWithEmail()) {
			if (user.isSendRegistrationMail()) {
				mailTemplate.sendTemplatedMail("adminNewRegistration.ftl", parameters, new String[] { user.getAdminEmail() }, user.getLocale());
			}
		}
		
		
		
	}

	@Observer(REGISTRATION_APPROVED)
	public void sendApproved(Registration registration, String reason) {
		String[] recipients = new String[] { registration.getEmail() };
		Map<String, Object> parameters = createMapForRegistrationMail(registration, reason);
		mailTemplate.sendTemplatedMail("registrationApproved.ftl", parameters, recipients, registration.getRequester().getLocale());
	}
	
	@Observer(REGISTRATION_DISAPPROVED)
	public void sendDisapproved(Registration registration, String reason) {
		String[] recipients = new String[]
			{ registration.getEmail() };
		Map<String, Object> parameters = createMapForRegistrationMail(registration, reason);
		mailTemplate.sendTemplatedMail("registrationDisapproved.ftl", parameters, recipients, registration.getRequester().getLocale());
	}
	
	private Map<String, Object> createMapForRegistrationMail(Registration registration, String reasonText) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("user", registration.getRequester());
		result.put("certificateDomain", registration.getCertificateDomain());
		result.put("reason", reasonText);

		return result;
	}
}
