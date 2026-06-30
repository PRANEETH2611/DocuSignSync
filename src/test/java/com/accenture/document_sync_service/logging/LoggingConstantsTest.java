package com.accenture.document_sync_service.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggingConstantsTest {

    @Test
    void shouldContainAuthenticationMessages() {

        assertNotNull(LoggingConstants.AUTH_START);
        assertNotNull(LoggingConstants.AUTH_SUCCESS);

        assertFalse(LoggingConstants.AUTH_START.isBlank());
        assertFalse(LoggingConstants.AUTH_SUCCESS.isBlank());
    }
}