package com.accenture.document_sync_service.service.impl;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.dto.event.DocuSignWebhookEvent;
import com.accenture.document_sync_service.service.ArchiveJobService;

import com.accenture.document_sync_service.service.WebhookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocuSignWebhookService implements WebhookService {

    private final ArchiveJobService archiveJobService;

    @Override
    public void processWebhook(DocuSignWebhookEvent event) {

        if (event == null
                || event.getData() == null
                || event.getData().getEnvelopeSummary() == null) {

            log.warn("Received invalid DocuSign webhook payload.");
            return;
        }

        String status =
                event.getData()
                        .getEnvelopeSummary()
                        .getStatus();

        if (!"completed".equalsIgnoreCase(status)) {

            log.info(
                    "Ignoring DocuSign event with status '{}'.",
                    status);

            return;
        }

        String envelopeId =
                event.getData()
                        .getEnvelopeId();

        log.info(
                "Processing completed envelope '{}'.",
                envelopeId);

        archiveJobService.savePendingJob(envelopeId);
    }
}