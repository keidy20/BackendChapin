package com.app.chapin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = "http://192.168.0.14:8084/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("foreachgt@gmail.com");
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, please click the link below:\n" + resetUrl);

        mailSender.send(message);
    }
}
