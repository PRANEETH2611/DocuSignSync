package com.accenture.document_sync_service.service;

import com.accenture.document_sync_service.dto.event.DocuSignWebhookEvent;

public interface WebhookService {

    void processWebhook(DocuSignWebhookEvent event);

}