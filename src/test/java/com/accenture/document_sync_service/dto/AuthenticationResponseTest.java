package com.accenture.document_sync_service.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationResponseTest {

    @Test
    void shouldStoreAuthenticationResponse() {

        AuthenticationResponse response =
                new AuthenticationResponse();

        response.setAccessToken("abc123");
        response.setTokenType("Bearer");
        response.setExpiresIn(3600L);

        assertEquals("abc123", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600L, response.getExpiresIn());
    }
}