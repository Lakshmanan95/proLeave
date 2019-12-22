package com.photon.vms.emailTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.photon.vms.common.exception.VmsApplicationException;
import com.photon.vms.common.exception.VmsLogging;

@Service
public class EmailService {
	static Properties emailProperties = new Properties();
	String emailPropertiesFile;
	String USER_NAME;
	String PASSWORD;
	String SMTP_PORT;
	String SMTP_HOST;
	String USERNAME_ONLY;
	String SENDER_EMAIL;
	
	@Autowired
	 private Environment env;
	
	@Autowired
	 JavaMailSender mailSender;
	
	public EmailService() {

		
	}
	public void initializeProperty() {
		try {		
			USER_NAME = env.getProperty("spring.mail.username");
			PASSWORD = env.getProperty("spring.mail.password");
			SMTP_HOST = env.getProperty("smtp.gmail.com");
			SMTP_PORT = env.getProperty("spring.mail.port");
			SENDER_EMAIL =  env.getProperty("spring.mail.username");
			
			String[] user = USER_NAME.split("@");
			USERNAME_ONLY = user[0];

		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Initialize Property Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	public void sendEmail(String emailTo, String emailSubject, String emailBodyMessage)  {
		try{
		initializeProperty();
		SMTPAuthenticator auth = new SMTPAuthenticator();
		Session session = Session.getInstance(emailProperties, auth);
		session.setDebug(false);
		MimeMessage msg = new MimeMessage(session);
		msg.setText(emailBodyMessage);
		msg.setSubject(emailSubject);
		msg.setFrom(new InternetAddress(SENDER_EMAIL));
		msg.setContent(emailBodyMessage,"text/html");
		
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress("gajalakshmi.giriraj@photoninfotech.net"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress("selvaganesh.mathiyaru@photoninfotech.net"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress("josephin.venisha@photoninfotech.net"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress("lakshmanan.j@photoninfotech.net"));
//		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
		mailSender.send(msg);
		
		} catch(Exception e){
			VmsLogging.logError(getClass(), "Email  - info Exception ", new VmsApplicationException(e.getMessage()));
		}

	}

	private class SMTPAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(USER_NAME, PASSWORD);
		}
	}
	
	public void sendMultipleToEmail(List<String> emailTO, String emailSubject, String emailBodyMessage) {
		initializeProperty();
		SMTPAuthenticator auth = new SMTPAuthenticator();
		Session session = Session.getInstance(emailProperties, auth);
		session.setDebug(false);
		List<String> email = new ArrayList<String>();
		System.out.println("Mail in service");
		email.add("gajalakshmi.giriraj@photoninfotech.net");
		email.add("lakshmanan.j@photoninfotech.net");
		email.add("selvaganesh.mathiyaru@photoninfotech.net");
		email.add("josephin.venisha@photoninfotech.net");
		try {
			MimeMessage message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(SENDER_EMAIL));
		    message.setContent(emailBodyMessage,"text/html");
		    for(String emailTo : email) {
		    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));	
		    }
		    message.setSubject(emailSubject);
		    VmsLogging.logInfo(getClass(), "content type "+message.getContentType());
		    mailSender.send(message);
		    VmsLogging.logInfo(getClass(), "Email for Multiple Revocation Appprove Sent");
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Email  - Leave Revocation Manager info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
	
	public void sendEmailWithAttachement(String emailTo, String emailSubject, String emailBodyMessage,String attachement) {
		initializeProperty();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
		    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
		    InternetAddress ad = new InternetAddress(emailTo);
		    message.setFrom(new InternetAddress(SENDER_EMAIL));
		    message.addTo("josephin.venisha@photoninfotech.net");
		    message.addTo("lakshmanan.j@photoninfotech.net");
		    message.addTo("gajalakshmi.giriraj@photoninfotech.net");
//		    message.addTo(ad);
		    SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy");
		    message.setSubject(emailSubject+"_"+dateFormat.format(new Date()));
		    message.setText(emailBodyMessage, true);
//		    message.setText(emailBodyMessage);
		    SimpleDateFormat formatter=new SimpleDateFormat("MM-dd-yyyy");
		    String fileName = "Vacation Management_Bulk Upload_"+formatter.format(new Date())+".csv";
		    message.addAttachment(fileName,  new ByteArrayResource(attachement.getBytes()));
		    mailSender.send(mimeMessage);
		} catch (Exception e) {
			VmsLogging.logError(getClass(), "Email  - Leave Credit Log info Exception ", new VmsApplicationException(e.getMessage()));
		}
	}
}
