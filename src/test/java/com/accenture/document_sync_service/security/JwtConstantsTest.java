package com.accenture.document_sync_service.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtConstantsTest {

    @Test
    void shouldContainExpectedConstants() {

        assertEquals("signature impersonation", JwtConstants.SCOPE);
        assertEquals(3600L, JwtConstants.EXPIRATION_SECONDS);
        assertEquals("RS256", JwtConstants.ALGORITHM);
    }
}