package com.accenture.document_sync_service.notification.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.accenture.document_sync_service.notification.config.EmailProperties;
import com.accenture.document_sync_service.notification.exception.EmailNotificationException;
import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.template.FailureEmailTemplateBuilder;
import com.accenture.document_sync_service.notification.template.SuccessEmailTemplateBuilder;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(SpringExtension.class)
class SpringMailEmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailProperties emailProperties;

    @Mock
    private SuccessEmailTemplateBuilder successTemplateBuilder;

    @Mock
    private FailureEmailTemplateBuilder failureTemplateBuilder;

    @InjectMocks
    private SpringMailEmailService emailService;

    private ExecutionSummary summary;

    @BeforeEach
    void setUp() {

        summary = new ExecutionSummary();

        when(emailProperties.getFrom())
                .thenReturn("from@test.com");

        when(emailProperties.getTo())
                .thenReturn(List.of("to@test.com"));

        when(emailProperties.getCc())
                .thenReturn(List.of());

        when(emailProperties.getBcc())
                .thenReturn(List.of());

        when(emailProperties.getSuccessSubject())
                .thenReturn("Success");

        when(emailProperties.getFailureSubject())
                .thenReturn("Failure");
    }
    @Test
void shouldSendSuccessEmail() {

    MimeMessage mimeMessage =
            new MimeMessage(Session.getDefaultInstance(new Properties()));

    when(mailSender.createMimeMessage())
            .thenReturn(mimeMessage);

    when(successTemplateBuilder.build(summary))
            .thenReturn("<html>Success</html>");

    emailService.sendSuccessEmail(summary);

    verify(mailSender).send(any(MimeMessage.class));
}
@Test
void shouldSendFailureEmail() {

    MimeMessage mimeMessage =
            new MimeMessage(Session.getDefaultInstance(new Properties()));

    when(mailSender.createMimeMessage())
            .thenReturn(mimeMessage);

    when(failureTemplateBuilder.build(summary))
            .thenReturn("<html>Failure</html>");

    emailService.sendFailureEmail(summary);

    verify(mailSender).send(any(MimeMessage.class));
}
@Test
void shouldThrowEmailNotificationExceptionWhenMailFails() {

    MimeMessage mimeMessage =
            new MimeMessage(Session.getDefaultInstance(new Properties()));

    when(mailSender.createMimeMessage())
            .thenReturn(mimeMessage);

    when(successTemplateBuilder.build(summary))
            .thenReturn("<html>Success</html>");

    doThrow(new MailSendException("SMTP Error"))
            .when(mailSender)
            .send(any(MimeMessage.class));

    assertThrows(
            EmailNotificationException.class,
            () -> emailService.sendSuccessEmail(summary));
}
}
