package com.accenture.document_sync_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "document-sync.scheduler")
public class SchedulerProperties {

    private int retryLimit = 3;
    private int fixedDelay = 60000;

}