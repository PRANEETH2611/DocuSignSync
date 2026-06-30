package com.accenture.document_sync_service.dto;

import java.time.Instant;

/**
 * Represents a DocuSign OAuth access token.
 */
public record AccessToken(
        String value,
        Instant expiresAt
) {
}