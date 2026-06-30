package com.accenture.document_sync_service.config;

import org.junit.jupiter.api.Test;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.*;

class TimeConfigurationTest {

    @Test
    void shouldCreateClockBean() {

        TimeConfiguration configuration = new TimeConfiguration();

        Clock clock = configuration.systemClock();

        assertNotNull(clock);
    }
}