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
package be.fedict.eid.pkira.blm.model.mail;

import java.util.Map;

/**
 * EJB wrapper around a freemarker mail template
 * 
 * @author Jan Van den Bergh
 */
public interface MailTemplate {

	static String NAME = "be.fedict.eid.pkira.blm.mailTemplate";

	/**
	 * Creates a mail message from a template.
	 * 
	 * @param templateName
	 *            name of the template (read from the mail/ resource folder).
	 * @param parameters
	 *            parameters to replace in the certificate.
	 * @return the mail.
	 */
	String createMailMessage(String templateName, Map<String, Object> parameters);

	/**
	 * Sends a mail created from a template.
	 * 
	 * @param templateName
	 *            name of the template (read from the mail/ resource folder).
	 * @param parameters
	 *            parameters to replace in the certificate.
	 * @param recipients
	 *            recipients of the mail.
	 */
	void sendTemplatedMail(String templateName, Map<String, Object> parameters, String[] recipients);
	
	/**
	 * Sends a mail created from a template.
	 * 
	 * @param templateName
	 *            name of the template (read from the mail/ resource folder).
	 * @param parameters
	 *            parameters to replace in the certificate.
	 * @param recipients
	 *            recipients of the mail.
	 */
	void sendTemplatedMail(String templateName, Map<String, Object> parameters, String[] recipients, byte[] attachmentData, String attachmentContentType, String attachmentFileName);
}
