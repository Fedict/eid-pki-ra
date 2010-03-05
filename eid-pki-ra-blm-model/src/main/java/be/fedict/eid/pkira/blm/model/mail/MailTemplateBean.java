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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Implementation of the mail template.
 * 
 * @author Jan Van den Bergh
 */
@Stateless
@Name(MailTemplate.NAME)
public class MailTemplateBean implements MailTemplate {

	@In(value = MailSender.NAME, create = true)
	private MailSender mailSender;

	@Logger
	private Log log;

	private Configuration configuration;

	@PostConstruct
	public void initialize() {
		configuration = new Configuration();

		configuration.setClassForTemplateLoading(getClass(), "/mail");
		configuration.setObjectWrapper(new DefaultObjectWrapper());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createMailMessage(String templateName, Map<String, Object> parameters) {
		try {
			Writer out = new StringWriter();
			Template temp = configuration.getTemplate(templateName);
			temp.process(parameters, out);
			out.flush();

			return out.toString();
		} catch (IOException e) {
			throw new RuntimeException("Error creating mail message", e);
		} catch (TemplateException e) {
			throw new RuntimeException("Error creating mail message", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendTemplatedMail(String templateName, Map<String, Object> parameters, String[] recipients,
			byte[] attachmentData, String attachmentContentType, String attachmentFileName) {
		try {
			// Create the message
			String processedTemplate = createMailMessage(templateName, parameters);
			String[] parts = processedTemplate.split("\\s*===\\s*");

			// Create the mail
			Mail mail = new Mail();
			mail.setSender(parts[0]);
			mail.setSubject(parts[1]);
			mail.setBody(parts[2]);
			mail.setRecipients(recipients);

			if (attachmentData != null) {
				mail.setAttachment(attachmentContentType, attachmentFileName, attachmentData);
			}

			// Send it
			mailSender.sendMail(mail);
		} catch (IOException e) {
			log.error("Error sending mail with certificate", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendTemplatedMail(String templateName, Map<String, Object> parameters, String[] recipients) {
		this.sendTemplatedMail(templateName, parameters, recipients, null, null, null);
	}

	protected void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	protected void setLog(Log log) {
		this.log = log;
	}

}
