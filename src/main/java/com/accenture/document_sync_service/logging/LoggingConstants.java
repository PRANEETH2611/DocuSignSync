package com.accenture.document_sync_service.logging;

/**
 * Centralized log message templates used throughout the application.
 */
public final class LoggingConstants {

    private LoggingConstants() {
    }

    public static final String AUTH_START =
            "Starting DocuSign authentication.";

    public static final String AUTH_SUCCESS =
            "DocuSign authentication completed successfully.";

    public static final String DOWNLOAD_START =
            "Starting document download. EnvelopeId={}";

    public static final String DOWNLOAD_SUCCESS =
            "Document downloaded successfully. EnvelopeId={}";

    public static final String DOWNLOAD_FAILED =
            "Document download failed. EnvelopeId={}";
}