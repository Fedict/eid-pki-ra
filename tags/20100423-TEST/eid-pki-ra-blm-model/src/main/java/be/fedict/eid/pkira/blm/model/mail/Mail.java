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

import java.io.Serializable;

/**
 * @author hans
 */
public class Mail implements Serializable {

	private static final long serialVersionUID = -2035797575503987602L;

	private String sender;
	private String[] recipients;
	private String subject;
	private String body;
	private String contentType = "text/html";
	private String attachmentContentType;
	private String attachmentFileName;
	private byte[] attachmentData;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	protected String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachment(String contentType, String fileName, byte[] data) {
		this.attachmentContentType = contentType;
		this.attachmentFileName = fileName;
		this.attachmentData = data;
	}

	public byte[] getAttachmentData() {
		return attachmentData;
	}

	protected static long getSerialversionuid() {
		return serialVersionUID;
	}

	protected String getAttachmentContentType() {
		return attachmentContentType;
	}

	
	public String getContentType() {
		return contentType;
	}

	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
