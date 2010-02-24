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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import be.fedict.eid.blm.model.ContractParserBean;

/**
 * @author hans
 */
@Stateless
public class MailSenderBean implements MailSender {

	private static Logger LOGGER = Logger.getLogger(MailSenderBean.class.getName());
	
	@Resource(mappedName = "java:JmsXA")
	private QueueConnectionFactory queueConnectionFactory;

	@Resource(mappedName = "mail-queue")
	private Queue queue;

	public QueueConnectionFactory getQueueConnectionFactory() {
		return queueConnectionFactory;
	}

	public void setQueueConnectionFactory(QueueConnectionFactory queueConnectionFactory) {
		this.queueConnectionFactory = queueConnectionFactory;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * be.fedict.eid.blm.mail.MailSender#sendMail(be.fedict.eid.blm.mail.Mail)
	 */
	@Override
	public void sendMail(Mail mail) {
		try {
			QueueConnection queueConnection = this.queueConnectionFactory.createQueueConnection();
			try {
				QueueSession queueSession = queueConnection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
				try {
					TextMessage textMessage = queueSession.createTextMessage();
					
					textMessage.setStringProperty("sender", mail.getSender());
					textMessage.setStringProperty("recipient", mail.getRecipient());
					textMessage.setStringProperty("subject", mail.getSubject());
					textMessage.setText( mail.getBody());
					
					
					QueueSender queueSender = queueSession.createSender(this.queue);
					try {
						queueSender.send(textMessage);
					} finally {
						queueSender.close();
					}
				} finally {
					queueSession.close();
				}
			} finally {
				queueConnection.close();
			}
		} catch (JMSException exception) {
			LOGGER.log(Level.SEVERE, "Cannot send message to the queue", exception);
			throw new RuntimeException(exception);
		}
	}
}
