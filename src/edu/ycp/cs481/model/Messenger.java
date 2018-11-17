package edu.ycp.cs481.model;

import java.util.*; 
import javax.mail.*; 
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

//send_email_gmail
public class Messenger {
	
	public Messenger(String recipient, String message) {
		 
	}	
	
	public static void main(String[] args) {
		/*
		 * args[0] = to (email)
		 * args[1] = Subject
		 * args[2] = Message
		 * 
		 * Call this method in a required class using
		 * Messenger.main(new String[] {"", "", ""});
		 * 
		 * */
		final String username = "ctm.mkii@gmail.com";
		final String password = "cS4B1SrS0ft!";
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
			message.setFrom(new InternetAddress("mailTester481@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(setRecipient(args[0])));
			message.setSubject(args[1]);
			message.setText(setMessage(args[2]));
			Transport.send(message);
			System.out.println("Message Sent");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String setRecipient(String name) {
		InternetAddress i = new InternetAddress(); 
		i.setAddress(name);
		return i.getAddress();
	}
	
	public static String setMessage(String text) {
		return text; 
	}
	
	public static InternetAddress setSender(String name) {
		InternetAddress i = new InternetAddress(); 
		i.setAddress(name);
		return i;
	}
	
	
}
