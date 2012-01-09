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

	private static String[][] htmlEscape =
		{
			{ "&lt;", "<" },
			{ "&gt;", ">" },
			{ "&amp;", "&" },
			{ "&quot;", "\"" },
			{ "&agrave;", "ˆ" },
			{ "&Agrave;", "Ë" },
			{ "&acirc;", "‰" },
			{ "&auml;", "Š" },
			{ "&Auml;", "€" },
			{ "&Acirc;", "å" },
			{ "&aring;", "Œ" },
			{ "&Aring;", "" },
			{ "&aelig;", "¾" },
			{ "&AElig;", "®" },
			{ "&ccedil;", "" },
			{ "&Ccedil;", "‚" },
			{ "&eacute;", "Ž" },
			{ "&Eacute;", "ƒ" },
			{ "&egrave;", "" },
			{ "&Egrave;", "é" },
			{ "&ecirc;", "" },
			{ "&Ecirc;", "æ" },
			{ "&euml;", "‘" },
			{ "&Euml;", "è" },
			{ "&iuml;", "•" },
			{ "&Iuml;", "ì" },
			{ "&ocirc;", "™" },
			{ "&Ocirc;", "ï" },
			{ "&ouml;", "š" },
			{ "&Ouml;", "…" },
			{ "&oslash;", "¿" },
			{ "&Oslash;", "¯" },
			{ "&szlig;", "§" },
			{ "&ugrave;", "" },
			{ "&Ugrave;", "ô" },
			{ "&ucirc;", "ž" },
			{ "&Ucirc;", "ó" },
			{ "&uuml;", "Ÿ" },
			{ "&Uuml;", "†" },
			{ "&nbsp;", " " },
			{ "&copy;", "\u00a9" },
			{ "&reg;", "\u00ae" },
			{ "&euro;", "\u20a0" } };

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
