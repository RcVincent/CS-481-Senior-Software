package edu.ycp.cs481.model;

import java.util.*; 
import javax.mail.*; 
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

//send_email_gmail
public class Messenger {
	public static void main(String[] args) {
		final String username = "rvincent@ycp.edu";
		final String password = "Cdcs1994bkk";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
			}
		  });
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("rvincent@ycp.edu"));
			message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse("vinsanity642@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!"
				+ "Visit http://chillyfacts.com");
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
