package com.accenture.document_sync_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.accenture.document_sync_service.dto.event.DocuSignWebhookData;
import com.accenture.document_sync_service.dto.event.DocuSignWebhookEvent;
import com.accenture.document_sync_service.dto.event.EnvelopeSummary;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.service.ArchiveJobService;

class DocuSignWebhookServiceTest {

    @Test
    void shouldSavePendingJobForCompletedEnvelope() {

        ArchiveJobService archiveJobService =
                mock(ArchiveJobService.class);

        DocuSignWebhookService service =
                new DocuSignWebhookService(archiveJobService);

        EnvelopeSummary summary = new EnvelopeSummary();
        summary.setStatus("completed");

        DocuSignWebhookData data = new DocuSignWebhookData();
        data.setEnvelopeId("envelope-123");
        data.setEnvelopeSummary(summary);

        DocuSignWebhookEvent event = new DocuSignWebhookEvent();
        event.setEvent("envelope-completed");
        event.setData(data);

        service.processWebhook(event);

        verify(archiveJobService)
                .savePendingJob("envelope-123");
    }

    @Test
    void shouldIgnoreNonCompletedEnvelope() {

        ArchiveJobService archiveJobService =
                mock(ArchiveJobService.class);

        DocuSignWebhookService service =
                new DocuSignWebhookService(archiveJobService);

        EnvelopeSummary summary = new EnvelopeSummary();
        summary.setStatus("sent");

        DocuSignWebhookData data = new DocuSignWebhookData();
        data.setEnvelopeId("envelope-123");
        data.setEnvelopeSummary(summary);

        DocuSignWebhookEvent event = new DocuSignWebhookEvent();
        event.setData(data);

        service.processWebhook(event);

        verify(archiveJobService, never())
                .savePendingJob("envelope-123");
    }

    @Test
    void shouldIgnoreNullEvent() {

        ArchiveJobService archiveJobService =
                mock(ArchiveJobService.class);

        DocuSignWebhookService service =
                new DocuSignWebhookService(archiveJobService);

        assertDoesNotThrow(() ->
                service.processWebhook(null));

        verify(archiveJobService, never())
                .savePendingJob("envelope-123");
    }

    @Test
    void shouldIgnoreMissingEnvelopeSummary() {

        ArchiveJobService archiveJobService =
                mock(ArchiveJobService.class);

        DocuSignWebhookService service =
                new DocuSignWebhookService(archiveJobService);

        DocuSignWebhookData data = new DocuSignWebhookData();
        data.setEnvelopeId("envelope-123");

        DocuSignWebhookEvent event = new DocuSignWebhookEvent();
        event.setData(data);

        service.processWebhook(event);

        verify(archiveJobService, never())
                .savePendingJob("envelope-123");
    }

    @Test
    void shouldPropagatePersistenceFailure() {

        ArchiveJobService archiveJobService =
                mock(ArchiveJobService.class);

        DocuSignWebhookService service =
                new DocuSignWebhookService(archiveJobService);

        EnvelopeSummary summary = new EnvelopeSummary();
        summary.setStatus("completed");

        DocuSignWebhookData data = new DocuSignWebhookData();
        data.setEnvelopeId("envelope-123");
        data.setEnvelopeSummary(summary);

        DocuSignWebhookEvent event = new DocuSignWebhookEvent();
        event.setData(data);

        doThrow(new DocumentSyncException("Persistence failed"))
                .when(archiveJobService)
                .savePendingJob("envelope-123");

        assertThrows(
                DocumentSyncException.class,
                () -> service.processWebhook(event));
    }
}