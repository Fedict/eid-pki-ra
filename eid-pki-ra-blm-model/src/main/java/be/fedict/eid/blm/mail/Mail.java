package be.fedict.eid.blm.mail;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Mail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5630695549821167339L;

	private String sender;
	private List<String> reciepients;
	private String subject;
	private String body;
	private List<Attachment> attachments;

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	public List<String> getReciepients() {
		return reciepients;
	}

	public void setReciepients(List<String> reciepients) {
		this.reciepients = reciepients;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

}
