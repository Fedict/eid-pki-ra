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

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;


/**
 * @author hans
 *
 */
@MessageDriven(activationConfig={
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "mail-queue")})
public class MailHandlerBean implements MessageListener {

	private static Logger LOGGER = Logger.getLogger(MailHandlerBean.class.getName());
	
	private String smtpServer;

	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message arg0) {
		TextMessage textMessage = (TextMessage) arg0;
		
		Properties props = new Properties();
		props.put("mail.smtp.host", getSmtpServer()); // retrieve from admin config
		try {
			props.put("mail.from", arg0.getStringProperty("sender"));

		Session session = Session.getInstance(props, null);

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom();
		msg.setRecipients(RecipientType.TO, arg0.getStringProperty("recipient"));
		msg.setSubject(arg0.getStringProperty("subject"));
		msg.setSentDate(new Date());
		msg.setText(textMessage.getText());
		Transport.send(msg);
		
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, "Cannot consume message from the queue", e);
			throw new RuntimeException(e);
		} // retrieve from admin config
		catch (MessagingException e) {
			LOGGER.log(Level.SEVERE, "Cannot send a mail message", e);
			throw new RuntimeException(e);
		}
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

}
