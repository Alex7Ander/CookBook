package ru.pavlov.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String username;
	
	public void send(String emailTo, String subject, String message) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(username);
		msg.setTo(emailTo);
		msg.setSubject(subject);
		msg.setText(message);
		
		mailSender.send(msg);
	}
}
