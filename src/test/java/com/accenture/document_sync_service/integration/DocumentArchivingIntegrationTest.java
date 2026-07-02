package com.accenture.document_sync_service.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.accenture.document_sync_service.config.GoogleCloudStorageProperties;
import com.accenture.document_sync_service.service.DocumentArchiveService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;

@SpringBootTest
class DocumentArchivingIntegrationTest {

    @Autowired
    private DocumentArchiveService archiveService;

    @Autowired
    private Storage storage;

    @Autowired
    private GoogleCloudStorageProperties properties;

    @Test
    void shouldArchiveCompletedDocument() {

        // Replace with a real completed envelope in your DocuSign sandbox
        String envelopeId = "19612a77-06c2-8a87-805f-c7d732661ec8";

        archiveService.archiveCompletedDocument(
                envelopeId
        );

        Blob blob = storage.get(
                properties.getBucketName(),
                envelopeId + ".pdf"
        );

        assertNotNull(blob);

        assertTrue(
                storage.delete(
                        properties.getBucketName(),
                        envelopeId + ".pdf"
                )
        );
    }
}