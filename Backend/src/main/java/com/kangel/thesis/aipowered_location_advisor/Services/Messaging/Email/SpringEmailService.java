package com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.EmailMessage;

import jakarta.mail.internet.MimeMessage;

@Service
public class SpringEmailService implements EmailService {

    private final JavaMailSender mailSender;

    public SpringEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void SendMail(EmailMessage request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), request.isHtml());

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
