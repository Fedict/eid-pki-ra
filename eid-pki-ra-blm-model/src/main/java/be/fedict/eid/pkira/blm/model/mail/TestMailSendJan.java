package be.fedict.eid.pkira.blm.model.mail;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;

public class TestMailSendJan {


	public static void main(String[] args) throws MessagingException {
		// Get properties
		String mailProtocol = "smtps";
		String mailServer = "smtp.gmail.com";
		String mailUser = "jan.vdbergh@gmail.com";
		String mailPort = "465";
		String mailPassword = "Hl4sP.3t";

		// Initialize a mail session
		Properties props = new Properties();
		props.put("mail." + mailProtocol + ".host", mailServer);
		props.put("mail." + mailProtocol + ".port", mailPort);
		Session session = Session.getInstance(props);

		// Create the message
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("jan.vdbergh@gmail.com"));
		msg.addRecipient(RecipientType.TO, new InternetAddress("j.vandenbergh@aca-it.be"));

		msg.setSubject("1 2 3 Test 1 2 3");

		Multipart multipart = new MimeMultipart();
		msg.setContent(multipart);

		// Set the email message text and attachment
		MimeBodyPart messagePart = new MimeBodyPart();
		messagePart.setContent("Test 1 2 3", "text/html");
		multipart.addBodyPart(messagePart);

		// Open transport and send message
		Transport transport = session.getTransport(mailProtocol);
		if (StringUtils.isNotBlank(mailUser)) {
			transport.connect(mailUser, mailPassword);
		} else {
			transport.connect();
		}
		msg.saveChanges();
		transport.sendMessage(msg, msg.getAllRecipients());
	}

}
