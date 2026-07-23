package com.accenture.document_sync_service.service;

public interface EnvelopeArchiveService {

    boolean isArchived(String envelopeId);

    void archive(
            String envelopeId,
            String objectName);
}