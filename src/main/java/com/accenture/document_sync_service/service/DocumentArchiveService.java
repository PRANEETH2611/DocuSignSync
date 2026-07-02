package com.accenture.document_sync_service.service;

public interface DocumentArchiveService {

    void archiveCompletedDocument(
            String envelopeId
    );

}