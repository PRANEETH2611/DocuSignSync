package com.accenture.document_sync_service.notification.service;

import java.nio.charset.StandardCharsets;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.notification.config.EmailProperties;
import com.accenture.document_sync_service.notification.exception.EmailNotificationException;
import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.template.FailureEmailTemplateBuilder;
import com.accenture.document_sync_service.notification.template.SuccessEmailTemplateBuilder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpringMailEmailService implements EmailService {

    private final JavaMailSender mailSender;

    private final EmailProperties emailProperties;

    private final SuccessEmailTemplateBuilder successTemplateBuilder;

    private final FailureEmailTemplateBuilder failureTemplateBuilder;

    @Override
public void sendSuccessEmail(ExecutionSummary summary) {
    String html = successTemplateBuilder.build(summary);

    sendEmail(
            emailProperties.getSuccessSubject(),
            html);
}

@Override
public void sendFailureEmail(ExecutionSummary summary) {
    String html = failureTemplateBuilder.build(summary);

    sendEmail(
            emailProperties.getFailureSubject(),
            html);
}


    private void sendEmail(String subject, String html) {

    try {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                true,
                StandardCharsets.UTF_8.name());

        helper.setFrom(emailProperties.getFrom());

        helper.setTo(emailProperties.getTo().toArray(new String[0]));

        if (emailProperties.getCc() != null && !emailProperties.getCc().isEmpty()) {
            helper.setCc(emailProperties.getCc().toArray(new String[0]));
        }

        if (emailProperties.getBcc() != null && !emailProperties.getBcc().isEmpty()) {
            helper.setBcc(emailProperties.getBcc().toArray(new String[0]));
        }

        helper.setSubject(subject);

        helper.setText(html, true);

        mailSender.send(message);

    } catch (MessagingException | MailException ex) {

        throw new EmailNotificationException(
                "Failed to send notification email",
                ex);

    }
}
}
