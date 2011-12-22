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
package be.fedict.eid.pkira.blm.model.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import be.fedict.eid.pkira.blm.errorhandling.ApplicationComponent;
import be.fedict.eid.pkira.blm.errorhandling.ErrorLogger;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryKey;
import be.fedict.eid.pkira.blm.model.config.ConfigurationEntryQuery;

/**
 * @author hans
 */
@MessageDriven(activationConfig =
	{ @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.queue"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "mail-queue") })
@Name(MailHandlerBean.NAME)
public class MailHandlerBean implements MessageListener {

	public static final String NAME = "be.fedict.eid.pkira.blm.mailHandlerBean";

	@In(value = ErrorLogger.NAME, create = true)
	private ErrorLogger errorLogger;

	@In(value = ConfigurationEntryQuery.NAME, create = true)
	private ConfigurationEntryQuery configurationEntryQuery;

	/*
	 * (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void onMessage(Message arg0) {
		ObjectMessage objectMessage = (ObjectMessage) arg0;
		try {
			Mail mail = (Mail) objectMessage.getObject();			

			// Get properties
			String mailProtocol = getMailProtocol();
			String mailServer = getSmtpServer();
			String mailUser = getSmtpUser();
			String mailPort = getSmtpPort();
			String mailPassword = getSmtpPassword();

			// Initialize a mail session
			Properties props = new Properties();
			props.put("mail." + mailProtocol + ".host", mailServer);
			props.put("mail." + mailProtocol + ".port", mailPort);
			Session session = Session.getInstance(props);

			// Create the message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mail.getSender()));
			for (String recipient : mail.getRecipients()) {
				msg.addRecipient(RecipientType.TO, new InternetAddress(recipient));
			}
			msg.setSubject(mail.getSubject(), "UTF-8");

			Multipart multipart = new MimeMultipart();
			msg.setContent(multipart);

			// Set the email message text and attachment
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setContent(mail.getBody(), mail.getContentType());
			multipart.addBodyPart(messagePart);

			if (mail.getAttachmentData() != null) {
				ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(mail.getAttachmentData(),
						mail.getAttachmentContentType());
				DataHandler dataHandler = new DataHandler(byteArrayDataSource);
				MimeBodyPart attachmentPart = new MimeBodyPart();
				attachmentPart.setDataHandler(dataHandler);
				attachmentPart.setFileName(mail.getAttachmentFileName());

				multipart.addBodyPart(attachmentPart);
			}

			// Open transport and send message
			Transport transport = session.getTransport(mailProtocol);
			if (StringUtils.isNotBlank(mailUser)) {
				transport.connect(mailUser, mailPassword);
			} else {
				transport.connect();
			}
			msg.saveChanges();
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (JMSException e) {
			errorLogger.logError(ApplicationComponent.MAIL, "Cannot handle the object message from the queue", e);
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			errorLogger.logError(ApplicationComponent.MAIL, "Cannot send a mail message", e);
			throw new RuntimeException(e);
		}
	}

	private String getMailProtocol() {
		return getMailProperty(ConfigurationEntryKey.MAIL_PROTOCOL);
	}

	public String getSmtpServer() {
		return getMailProperty(ConfigurationEntryKey.MAIL_SERVER);
	}

	public String getSmtpPort() {
		return getMailProperty(ConfigurationEntryKey.MAIL_PORT);
	}

	public String getSmtpUser() {
		return getMailProperty(ConfigurationEntryKey.MAIL_USER);
	}

	public String getSmtpPassword() {
		return getMailProperty(ConfigurationEntryKey.MAIL_PASSWORD);
	}

	private String getMailProperty(ConfigurationEntryKey configurationEntryKey) {
		return configurationEntryQuery.findByEntryKey(configurationEntryKey).getValue();
	}

}
