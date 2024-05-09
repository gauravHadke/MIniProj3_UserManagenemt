package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender; // impl class will inject 
	
	public boolean sendEmail(String to, String subject, String body) {
		boolean isMailSent = false;	
		try {
			MimeMessage mimeMsg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true); // use true for enable hyperlink to send	
			//helper.addAttachment(attachmentFileName, data);  for attachment to send
			
			mailSender.send(mimeMsg);
			
			isMailSent = true;
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
	  return isMailSent;
	}
}
