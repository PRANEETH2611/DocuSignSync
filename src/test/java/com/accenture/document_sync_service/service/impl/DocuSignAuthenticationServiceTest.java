package com.accenture.document_sync_service.service.impl;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.web.client.RestClient;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.security.jwt.JwtGenerator;

class DocuSignAuthenticationServiceTest {

    @Test
    void shouldPropagateJwtGenerationFailure() {

        JwtGenerator jwtGenerator = mock(JwtGenerator.class);
        RestClient restClient = mock(RestClient.class);
        DocusignProperties properties = new DocusignProperties();
        Clock clock = Clock.systemUTC();

        when(jwtGenerator.generateJwt())
                .thenThrow(new DocumentSyncException("JWT generation failed"));

        DocuSignAuthenticationService service =
                new DocuSignAuthenticationService(
                        jwtGenerator,
                        restClient,
                        properties,
                        clock
                        
                );

        assertThrows(
                DocumentSyncException.class,
                service::getAccessToken
        );
    }
}