package com.accenture.document_sync_service.exception;

public class DocumentDownloadException extends DocumentSyncException {

    public DocumentDownloadException(String message) {
        super(message);
    }

    public DocumentDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}