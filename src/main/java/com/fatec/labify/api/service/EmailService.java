package com.fatec.labify.api.service;

import com.fatec.labify.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private static final String FROM = "labify.contato@gmail.com";
    private static final String FROM_NAME = "Labify";

    public static final String URL = "http://localhost:8080";

    public EmailService(JavaMailSender emailSender) { this.emailSender = emailSender; }

    @Async
    private void sendEmail(String userEmail, String subject, String content) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(FROM, FROM_NAME);
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao enviar email");
        }

        emailSender.send(message);
    }

    public void sendVerificationEmail(User user) {
        String subject = "Labify - Verifique seu e-mail";

        String content = generateEmailContent(
                "<div style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">" +
                        "<div style=\"max-width: 600px; background-color: #ffffff; padding: 30px; border-radius: 8px; margin: auto;\">" +
                        "<h2 style=\"color: #333333;\">Olá [[name]],</h2>" +
                        "<p>Seja bem-vindo(a) à <strong>Labify</strong>!</p>" +
                        "<p>Para ativar sua conta, clique no botão abaixo:</p>" +
                        "<p style=\"text-align: center;\">" +
                        "<a href=\"[[URL]]\" target=\"_blank\" " +
                        "style=\"background-color: #4CAF50; color: white; padding: 12px 25px; " +
                        "text-decoration: none; border-radius: 5px; display: inline-block; margin: 15px 0;\">" +
                        "Verificar meu e-mail</a>" +
                        "</p>" +
                        "<p style=\"font-size: 12px; text-align: center; color: #888888; margin-top: 15px;\">" +
                        "Se você não realizou este cadastro, ignore este e-mail." +
                        "</p>" +
                        "<p style=\"font-size: 10px; text-align: center; color: #888888; margin-top: 5px;\">" +
                        "© 2025 AgendaExames. Todos os direitos reservados." +
                        "</p>" +
                        "</div>" +
                        "</div>",
                user.getName(),
                URL + "/users/verify-account?code=" + user.getToken()
        );

        sendEmail(user.getEmail(), subject, content);
    }

    private String generateEmailContent(String template, String name, String url) {
        return template.replace("[[name]]", name).replace("[[URL]]", url);
    }
}
