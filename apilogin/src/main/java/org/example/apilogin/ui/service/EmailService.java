package org.example.apilogin.ui.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.apilogin.common.Constantes;
import org.example.apilogin.domain.errores.EmailException;
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
            MimeMessageHelper helper = new MimeMessageHelper(message, true, Constantes.CHARSET_UTF8);

            helper.setTo(email);
            helper.setSubject(Constantes.EMAIL_SUBJECT_ACTIVACION);

            Context context = new Context();
            context.setVariable(Constantes.PARAM_USUARIO, usuario);
            context.setVariable(Constantes.PARAM_CODIGO_ACTIVACION, codigoActivacion);
            context.setVariable(Constantes.PARAM_URL_ACTIVACION, Constantes.URL + Constantes.API_ACTIVAR_CUENTA + "?codigo=" + codigoActivacion);

            String htmlContent = templateEngine.process(Constantes.TEMPLATE_EMAIL_ACTIVACION, context);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailException(Constantes.EMAIL_ERROR_ENVIO);
        }
    }
}
