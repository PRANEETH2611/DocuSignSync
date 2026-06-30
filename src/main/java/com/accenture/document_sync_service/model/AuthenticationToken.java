package com.accenture.document_sync_service.model;

import java.time.Instant;

/**
 * Represents an authentication token returned by DocuSign.
 *
 * @param accessToken OAuth access token
 * @param expiresAt Token expiry time
 */
public record AuthenticationToken(
        String accessToken,
        Instant expiresAt
) {
}   