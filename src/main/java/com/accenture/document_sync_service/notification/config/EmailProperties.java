package com.accenture.document_sync_service.notification.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "notification.email")
public class EmailProperties {

    private String from;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;

    private String successSubject;
    private String failureSubject;
}