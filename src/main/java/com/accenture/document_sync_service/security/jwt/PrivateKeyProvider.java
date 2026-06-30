package com.accenture.document_sync_service.security.jwt;

import java.security.PrivateKey;

/**
 * Provides an RSA private key for JWT signing.
 */
public interface PrivateKeyProvider {

    PrivateKey getPrivateKey();
}