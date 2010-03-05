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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author hans
 */
@MessageDriven(activationConfig =
	{ @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.queue"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "mail-queue") })
public class MailHandlerBean implements MessageListener {

	private static Logger LOGGER = Logger.getLogger(MailHandlerBean.class.getName());

	// TODO: retrieve from admin config
	private String smtpServer = "mail.aca-it.be";
	// TODO: retrieve from admin config
	private String port = "25";

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

			// Initialize a mail session
			Properties props = new Properties();
			props.put("mail.smtp.host", getSmtpServer());
			props.put("mail.smtp.port", getPort());
			props.put("mail.from", mail.getSender());
			Session session = Session.getInstance(props, null);

			// Create the message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom();
			msg.setRecipients(RecipientType.TO, mail.getRecipient());
			msg.setSubject(mail.getSubject());
			
			Multipart multipart = new MimeMultipart();
			msg.setContent(multipart);

			// Set the email message text and attachment
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(mail.getBody(), "ISO8859-1", "html");
			multipart.addBodyPart(messagePart);

			if (mail.getAttachmentData()!=null) {
				InternetHeaders headers = new InternetHeaders();
				headers.setHeader("Content-type", mail.getAttachmentContentType());
				headers.setHeader("Content-Disposition", "attachment; filename=\"" + mail.getAttachmentFileName() + "\"");
				
				MimeBodyPart attachmentPart = new MimeBodyPart(headers, mail.getAttachmentData());				
				multipart.addBodyPart(attachmentPart);				
			}

			Transport.send(msg);
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, "Cannot handle the Object message from the queue", e);
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			LOGGER.log(Level.SEVERE, "Cannot send a mail message", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param smtpServer
	 */
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	/**
	 * @return
	 */
	public String getSmtpServer() {
		return smtpServer;
	}

	/**
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return
	 */
	public String getPort() {
		return port;
	}

}