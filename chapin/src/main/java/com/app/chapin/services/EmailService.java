package com.app.chapin.services;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("$(Educate)")
    private String fromEmail;

    public void sendEmail() {

        final String to = "jennercalito2016@gmail.com";
        final String subject = "Este es un ejemplo";
        final String htmlContent = "<h1>test de envio de correo</h1>";


        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indica que el contenido es HTML

            javaMailSender.send(message);
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }

    }

    public void sendPasswordResetEmail(final String to, final String token) {
        final String subject = "Password Reset";

        final String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Recuperación de Contraseña</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #fff;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #555;\n" +
                "            line-height: 1.6;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            font-size: 16px;\n" +
                "            font-weight: bold;\n" +
                "            color: #fff;\n" +
                "            background-color: #007bff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 12px;\n" +
                "            color: #888;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Recupera tu Contraseña</h1>\n" +
                "        <p>Hola,</p>\n" +
                "        <p>Recibimos una solicitud para restablecer la contraseña de tu cuenta. Si no solicitaste este cambio, por favor ignora este correo.</p>\n" +
                "        <p>Para restablecer tu contraseña, ingresa el siguiente token en tu aplicación movil:</p>\n" +
                "        <p>" + token + "</p>\n" +
                "        <p>Este token es válido por 1 hora.</p>\n" +
                "        <p>Saludos,<br>El equipo de soporte</p>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Si tienes alguna pregunta, no dudes en ponerte en contacto con nosotros.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indica que el contenido es HTML

            javaMailSender.send(message);
        } catch (Exception e) {
            log.info("Error al enviar el token: {}", e.getMessage());
        }
    }
}
