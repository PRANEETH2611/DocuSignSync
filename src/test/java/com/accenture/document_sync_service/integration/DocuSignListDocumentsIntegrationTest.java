package com.accenture.document_sync_service.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.accenture.document_sync_service.dto.docusign.EnvelopeDocument;
import com.accenture.document_sync_service.service.DownloadService;

@SpringBootTest
class DocuSignListDocumentsIntegrationTest {

    @Autowired
    private DownloadService downloadService;

    @Test
    void shouldListEnvelopeDocuments() {

        String envelopeId = "f1cd255f-b195-8669-818c-90a8fc7f0d90";

        List<EnvelopeDocument> documents = downloadService.listDocuments(envelopeId);

        assertNotNull(documents);
        assertFalse(documents.isEmpty());

        documents.forEach(document ->

        System.out.println(
                document.getDocumentId()
                        + " -> "
                        + document.getName()));
    }
}