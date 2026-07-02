package com.accenture.document_sync_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.document_sync_service.dto.event.DocuSignWebhookEvent;
import com.accenture.document_sync_service.service.WebhookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhooks")
public class DocuSignWebhookController {

    private final WebhookService webhookService;
    
    @PostMapping("/docusign")
    public ResponseEntity<Void> receiveWebhook(
        @RequestBody DocuSignWebhookEvent event) {

    log.info("Received DocuSign webhook.");

    webhookService.processWebhook(event);

    return ResponseEntity.ok().build();
}
}