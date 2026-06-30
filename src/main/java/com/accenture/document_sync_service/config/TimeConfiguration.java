package com.accenture.document_sync_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Provides the application's Clock bean.
 */
@Configuration
public class TimeConfiguration {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }
}