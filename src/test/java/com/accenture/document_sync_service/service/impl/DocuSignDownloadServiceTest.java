package com.accenture.document_sync_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.web.client.RestClient;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.service.AuthenticationService;

class DocuSignDownloadServiceTest {

    @Test
    void shouldPropagateAuthenticationFailure() {

        AuthenticationService authenticationService =
                mock(AuthenticationService.class);

        RestClient restClient =
                mock(RestClient.class);

        DocusignProperties properties =
                new DocusignProperties();

        when(authenticationService.getAccessToken())
                .thenThrow(
                        new DocumentSyncException("Authentication failed.")
                );

        DocuSignDownloadService service =
                new DocuSignDownloadService(
                        authenticationService,
                        restClient,
                        properties
                );

        assertThrows(
                DocumentSyncException.class,
                () -> service.downloadCompletedDocument("19612a77-06c2-8a87-805f-c7d732661ec8")
        );
    }
}