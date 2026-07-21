package com.accenture.document_sync_service.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.accenture.document_sync_service.service.DownloadService;

@SpringBootTest
class DocuSignDownloadDocumentIntegrationTest {

    @Autowired
    private DownloadService downloadService;

    @Test
    void shouldDownloadIndividualDocument() throws Exception {

        String envelopeId = "f1cd255f-b195-8669-818c-90a8fc7f0d90";

        InputStream inputStream = downloadService.downloadDocument(
                envelopeId,
                "1");

        assertNotNull(inputStream);

        assertTrue(inputStream.read() != -1);

        inputStream.close();
    }
}