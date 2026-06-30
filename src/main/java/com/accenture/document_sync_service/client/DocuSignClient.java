package com.accenture.document_sync_service.client;
import com.accenture.document_sync_service.model.AuthenticationToken;
/**
 * Defines operations supported by the DocuSign integration.
 *
 * Business services depend on this interface rather than the
 * DocuSign SDK implementation.
 */
public interface DocuSignClient {

    /**
     * Authenticates with DocuSign using JWT and returns an access token.
     *
     * @return JWT access token
     */
    AuthenticationToken authenticate();
}