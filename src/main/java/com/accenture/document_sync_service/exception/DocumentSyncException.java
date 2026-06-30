package com.accenture.document_sync_service.exception;

/**
 * Base exception for the Document Sync application.
 */
public class DocumentSyncException extends RuntimeException {

    public DocumentSyncException(String message) {
        super(message);
    }

    public DocumentSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}