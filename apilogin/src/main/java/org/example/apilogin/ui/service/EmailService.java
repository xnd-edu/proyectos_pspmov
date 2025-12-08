package org.example.apilogin.ui.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.apilogin.common.Constantes;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarEmailActivacion(String usuario, String email, String codigoActivacion) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            System.out.println("Email recibido: '" + email.replace("\n", "\\n").replace(" ", "_") + "'");

            helper.setTo(email);
            helper.setSubject("Activa tu cuenta - Código de activación");

            Context context = new Context();
            context.setVariable("usuario", usuario);
            context.setVariable("codigoActivacion", codigoActivacion);
            context.setVariable("urlActivacion", Constantes.URL + Constantes.API_ACTIVAR_CUENTA + "?codigo=" + codigoActivacion);

            String htmlContent = templateEngine.process("email-activacion", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email de activación", e);
        }
    }
}
