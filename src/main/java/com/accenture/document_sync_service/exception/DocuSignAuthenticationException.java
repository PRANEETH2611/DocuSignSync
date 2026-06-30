package com.accenture.document_sync_service.exception;

public class DocuSignAuthenticationException extends DocumentSyncException {

    public DocuSignAuthenticationException(String message) {
        super(message);
    }

    public DocuSignAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}