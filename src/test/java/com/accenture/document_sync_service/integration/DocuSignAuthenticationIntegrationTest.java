package com.accenture.document_sync_service.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.accenture.document_sync_service.dto.AccessToken;
import com.accenture.document_sync_service.service.AuthenticationService;

@SpringBootTest

class DocuSignAuthenticationIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void shouldAuthenticateSuccessfully() {

        AccessToken token = authenticationService.getAccessToken();

        assertNotNull(token);
        assertNotNull(token.value());
        assertFalse(token.value().isBlank());
    }
}