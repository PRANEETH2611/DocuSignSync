package com.accenture.document_sync_service.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenTest {

    @Test
    void shouldCreateAccessToken() {

        Instant expiry = Instant.now();

        AccessToken token =
                new AccessToken(
                        "sample-token",
                        expiry
                );

        assertEquals("sample-token", token.value());
        assertEquals(expiry, token.expiresAt());
    }
}