/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2012 FedICT.
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
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.apache.commons.lang.LocaleUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import be.fedict.eid.pkira.blm.errorhandling.ApplicationComponent;
import be.fedict.eid.pkira.blm.errorhandling.ErrorLogger;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;
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

	@In(value = ConfigurationEntryQuery.NAME, create = true)
	private ConfigurationEntryQuery configurationEntryQuery;

	@In(value = ErrorLogger.NAME, create = true)
	private ErrorLogger errorLogger;

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
	public String createMailMessage(String templateName, Map<String, Object> parameters, String localeStr) {
		parameters.put("configuration", configurationEntryQuery.getAsMap());

		Locale locale = Locale.ENGLISH;
		try {
			locale = LocaleUtils.toLocale(localeStr);
		} catch (Exception e) {
			log.error("Invalid locale string: " + localeStr, e);
		}

		try {
			Writer out = new StringWriter();
			Template temp = configuration.getTemplate(templateName, locale);
			temp.process(parameters, out);
			out.flush();

			return out.toString();
		} catch (IOException e) {
			errorLogger.logError(ApplicationComponent.MAIL, "Error creating mail message", e);
			return null;
		} catch (TemplateException e) {
			errorLogger.logError(ApplicationComponent.MAIL, "Error creating mail message", e);
			return null;
		}
	}

	public void setErrorLogger(ErrorLogger errorLogger) {
		this.errorLogger = errorLogger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendTemplatedMail(String templateName, Map<String, Object> parameters, String[] recipients,
			byte[] attachmentData, String attachmentContentType, String attachmentFileName, String locale) {
		if (locale == null) {
			locale = "en";
		}
		
		if (recipients == null || recipients.length == 0) {
			return;
		}

		try {
			// Create the message
			String processedTemplate = createMailMessage(templateName, parameters, locale);
			if (processedTemplate == null) {
				return;
			}
			String[] parts = processedTemplate.split("\\s*===\\s*");

			// Create the mail
			Mail mail = new Mail();
			mail.setContentType(parts[0]);
			mail.setSender(parts[1]);
			mail.setSubject(unescapeHTML(parts[2]));
			mail.setBody(parts[3]);
			mail.setRecipients(recipients);

			if (attachmentData != null) {
				mail.setAttachment(attachmentContentType, attachmentFileName, attachmentData);
			}

			// Send it
			mailSender.sendMail(mail);
		} catch (IOException e) {
			errorLogger.logError(ApplicationComponent.MAIL, "Error sending mail message", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendTemplatedMail(String templateName, Map<String, Object> parameters, String[] recipients,
			String locale) {
		this.sendTemplatedMail(templateName, parameters, recipients, null, null, null, locale);
	}

	protected void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setConfigurationEntryQuery(ConfigurationEntryQuery configurationEntryQuery) {
		this.configurationEntryQuery = configurationEntryQuery;
	}

	protected static String[][] htmlEscape =
		{
			{ "&lt;", "<" },
			{ "&gt;", ">" },
			{ "&amp;", "&" },
			{ "&quot;", "\"" },
			{ "&agrave;", "\u00E0" },
			{ "&Agrave;", "\u00C0" },
			{ "&acirc;", "\u00E2" },
			{ "&auml;", "\u00E4" },
			{ "&Auml;", "\u00C4" },
			{ "&Acirc;", "\u00C2" },
			{ "&aring;", "\u00E5" },
			{ "&Aring;", "\u00C5" },
			{ "&aelig;", "\u00E6" },
			{ "&AElig;", "\u00C6" },
			{ "&ccedil;", "\u00E7" },
			{ "&Ccedil;", "\u00C7" },
			{ "&eacute;", "\u00E9" },
			{ "&Eacute;", "\u00C9" },
			{ "&egrave;", "\u00E8" },
			{ "&Egrave;", "\u00C8" },
			{ "&ecirc;", "\u00EA" },
			{ "&Ecirc;", "\u00CA" },
			{ "&euml;", "\u00EB" },
			{ "&Euml;", "\u00CB" },
			{ "&iuml;", "\u00EF" },
			{ "&Iuml;", "\u00CF" },
			{ "&ocirc;", "\u00F4" },
			{ "&Ocirc;", "\u00D4" },
			{ "&ouml;", "\u00F6" },
			{ "&Ouml;", "\u00D6" },
			{ "&oslash;", "\u00F8" },
			{ "&Oslash;", "\u00D8" },
			{ "&szlig;", "\u00DF" },
			{ "&ugrave;", "\u00F9" },
			{ "&Ugrave;", "\u00D9" },
			{ "&ucirc;", "\u00FB" },
			{ "&Ucirc;", "\u00DB" },
			{ "&uuml;", "\u00FC" },
			{ "&Uuml;", "\u00DC" },
			{ "&nbsp;", " " },
			{ "&copy;", "\u00a9" },
			{ "&reg;", "\u00ae" },
			{ "&euro;", "\u20ac" } };

	public static final String unescapeHTML(String s) {
		StringBuilder builder = new StringBuilder(s);

		while (true) {
			int i = builder.indexOf("&");

			if (i > -1) {
				int j = builder.indexOf(";", i);
				if (j > i) {
					String temp = builder.substring(i, j + 1);
					String replacement = "?";
					for (String[] replacements : htmlEscape) {
						if (replacements[0].equals(temp)) {
							replacement = replacements[1];
							break;
						}
					}
					builder.replace(i, j + 1, replacement);
				} else {
					break;
				}
			} else {
				break;
			}
		}

		return builder.toString();
	}

}
