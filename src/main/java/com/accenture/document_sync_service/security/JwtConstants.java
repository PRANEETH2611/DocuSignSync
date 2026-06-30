package com.accenture.document_sync_service.security;

/**
 * Constants used for DocuSign JWT generation.
 */
public final class JwtConstants {

    private JwtConstants() {
    }

    /**
     * OAuth scope required for JWT Grant.
     */
    public static final String SCOPE = "signature impersonation";

    /**
     * JWT token validity in seconds.
     */
    public static final long EXPIRATION_SECONDS = 3600L;

    /**
     * JWT signing algorithm.
     */
    public static final String ALGORITHM = "RS256";
}