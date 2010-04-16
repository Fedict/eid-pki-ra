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
package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.async.QuartzTriggerHandle;

import be.fedict.eid.pkira.blm.model.contracts.Certificate;
import be.fedict.eid.pkira.blm.model.mail.MailTemplate;

/**
 * @author Jan Van den Bergh
 */
@Name(SchedulerBean.NAME)
@Scope(ScopeType.STATELESS)
public class SchedulerBean {

	public static final String NAME = "be.fedict.eid.pkira.blm.scheduler";

	@In(value = MailTemplate.NAME, create = true)
	private MailTemplate mailTemplate;

	@In(create = true)
	private QuartzTriggerHandle timer;

	@Asynchronous
	@Transactional(TransactionPropagationType.REQUIRED)
	public QuartzTriggerHandle scheduleNotifcation(@Expiration Date when, Certificate certificate,
			String mail) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("certificate", certificate);

		String[] email = new String[1];
		email[0] = mail;
		mailTemplate.sendTemplatedMail("certificateNearlyExpired.ftl", parameters, email);

		return timer;
	}
}
