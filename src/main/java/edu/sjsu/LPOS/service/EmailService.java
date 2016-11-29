package edu.sjsu.LPOS.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.User;

@Service
public class EmailService {
    @Autowired private JavaMailSender javaMailSender;

    @Async
    public void send(User user, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            String email = user.getEmail();
            if (email == null) {
            	email = user.getUsername();
            }
            helper.setTo(email);
            helper.setReplyTo("lunchplanningorderingservice@gmail.com");
            helper.setFrom("lunchplanningorderingservice@gmail.com");
            helper.setSubject("Account Confirmation");
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return;
    }
}
