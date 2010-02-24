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
package be.fedict.eid.blm.model.mail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePartDataSource;

/**
 * @author hans
 * 
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "mail-queue") })
public class MailHandlerBean implements MessageListener {

	private static Logger LOGGER = Logger.getLogger(MailHandlerBean.class
			.getName());

	// TODO: retrieve from admin config
	private String smtpServer;
	// TODO: retrieve from admin config
	private String port;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message arg0) {
		ObjectMessage objectMessage = (ObjectMessage) arg0;
		try {
			Mail mail = (Mail) objectMessage.getObject();

			Properties props = new Properties();
			props.put("mail.smtp.host", getSmtpServer());
			props.put("mail.smtp.port", getPort());

			props.put("mail.from", mail.getSender());

			Session session = Session.getInstance(props, null);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom();
			msg.setRecipients(RecipientType.TO, mail.getRecipient());
			msg.setSubject(mail.getSubject());
			msg.setSentDate(new Date());

			// Set the email message text and attachment
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(mail.getBody());

			InputStream inputStream = new ByteArrayInputStream(mail
					.getAttachment());
			MimeBodyPart attachmentPart = new MimeBodyPart(inputStream);

			MimePartDataSource mimePartDataSource = new MimePartDataSource(
					attachmentPart);

			attachmentPart.setDataHandler(new DataHandler(mimePartDataSource));

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messagePart);
			multipart.addBodyPart(attachmentPart);

			msg.setContent(multipart);

			Transport.send(msg);

		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE,
					"Cannot handle the Object message from the queue", e);
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			LOGGER.log(Level.SEVERE, "Cannot send a mail message", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param smtpServer
	 */
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	/**
	 * 
	 * @return
	 */
	public String getSmtpServer() {
		return smtpServer;
	}

	/**
	 * 
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * 
	 * @return
	 */
	public String getPort() {
		return port;
	}

}
