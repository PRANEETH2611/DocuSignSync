package com.accenture.document_sync_service.service;

import java.time.Instant;

public interface RecipientService {

    Instant getTenantSignedDate(
            String envelopeId);

}