package com.accenture.document_sync_service.service.impl;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.config.GoogleCloudStorageProperties;
import com.accenture.document_sync_service.dto.docusign.envelope.EnvelopeInfo;
import com.accenture.document_sync_service.entity.SchedulerCheckpoint;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.notification.enums.EnvironmentType;
import com.accenture.document_sync_service.notification.enums.JobStatus;
import com.accenture.document_sync_service.notification.enums.TriggerType;
import com.accenture.document_sync_service.notification.enums.UploadStatus;
import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.model.ProcessedEnvelopeSummary;
import com.accenture.document_sync_service.service.CheckpointService;
import com.accenture.document_sync_service.service.DownloadService;
import com.accenture.document_sync_service.service.EnvelopeArchiveService;
import com.accenture.document_sync_service.service.EnvelopePollingService;
import com.accenture.document_sync_service.service.PollingWorkflowService;
import com.accenture.document_sync_service.service.RecipientService;
import com.accenture.document_sync_service.service.UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollingWorkflowServiceImpl implements PollingWorkflowService {

        private final CheckpointService checkpointService;
        private final EnvelopePollingService envelopePollingService;
        private final DownloadService downloadService;
        private final UploadService uploadService;
        private final GoogleCloudStorageProperties storageProperties;
        private final EnvelopeArchiveService envelopeArchiveService;
        private final RecipientService recipientService;

        @Override
        public ExecutionSummary executePollingWorkflow() {

                log.info("Starting polling workflow.");

                Instant currentSchedulerRunTime = Instant.now();

                ExecutionSummary summary = new ExecutionSummary();

                summary.setJobId(UUID.randomUUID().toString());
                summary.setStartedAt(currentSchedulerRunTime);
                summary.setStatus(JobStatus.SUCCESS);
                summary.setEnvironment(EnvironmentType.DEV);
                summary.setTrigger(TriggerType.CLOUD_SCHEDULER);
                summary.setBucketName(storageProperties.getBucketName());

                List<ProcessedEnvelopeSummary> processedEnvelopes = new ArrayList<>();
                summary.setProcessedEnvelopes(processedEnvelopes);

                SchedulerCheckpoint checkpoint = checkpointService.getCheckpoint();

                summary.setPreviousCheckpoint(
                                checkpoint.getLastScheduledRunTime());

                List<EnvelopeInfo> envelopes = envelopePollingService.getCompletedEnvelopes(
                                checkpoint.getLastScheduledRunTime(),
                                currentSchedulerRunTime);

                summary.setEnvelopesFound(envelopes.size());

                if (envelopes.isEmpty()) {

                        checkpointService.updateCheckpoint(currentSchedulerRunTime);

                        summary.setNewCheckpoint(currentSchedulerRunTime);

                        Instant completedAt = Instant.now();

                        summary.setCompletedAt(completedAt);
                        summary.setDuration(
                                        Duration.between(
                                                        summary.getStartedAt(),
                                                        summary.getCompletedAt()));

                        log.info("No completed envelopes found.");

                        return summary;
                }

                log.info("Found {} completed envelope(s).", envelopes.size());

                for (EnvelopeInfo envelope : envelopes) {

                        if (envelopeArchiveService.isArchived(
                                        envelope.getEnvelopeId())) {

                                log.info(
                                                "Envelope '{}' already archived. Skipping.",
                                                envelope.getEnvelopeId());

                                continue;
                        }

                        try {

                                ProcessedEnvelopeSummary processed = processEnvelope(envelope);

                                envelopeArchiveService.archive(
                                                envelope.getEnvelopeId(),
                                                processed.getDocumentName());

                                processedEnvelopes.add(processed);

                                summary.setUploaded(
                                                summary.getUploaded() + 1);

                        } catch (Exception exception) {

                                summary.setFailed(
                                                summary.getFailed() + 1);

                                log.error(
                                                "Skipping envelope '{}' after processing failure.",
                                                envelope.getEnvelopeId(),
                                                exception);
                        }
                }

                checkpointService.updateCheckpoint(currentSchedulerRunTime);

                summary.setNewCheckpoint(currentSchedulerRunTime);

                Instant completedAt = Instant.now();

                summary.setCompletedAt(completedAt);

                summary.setDuration(
                                Duration.between(
                                                summary.getStartedAt(),
                                                completedAt));

                log.info("Polling workflow completed.");

                return summary;
        }

        private ProcessedEnvelopeSummary processEnvelope(EnvelopeInfo envelope) {
                Instant processingStart = Instant.now();

                log.info("Processing envelope '{}'.", envelope.getEnvelopeId());

                try (
                                InputStream inputStream = downloadService
                                                .downloadCompletedDocument(envelope.getEnvelopeId())) {
                        Instant tenantSigningDate = recipientService.getTenantSignedDate(
                                        envelope.getEnvelopeId());

                        String objectName = generateObjectName(
                                        envelope,
                                        tenantSigningDate);

                        uploadService.upload(objectName, inputStream, "application/pdf");
                        ProcessedEnvelopeSummary processed = new ProcessedEnvelopeSummary();

                        processed.setEnvelopeId(envelope.getEnvelopeId());
                        processed.setEnvelopeSubject(envelope.getEmailSubject());
                        processed.setDocumentName(objectName);
                        processed.setCompletedAt(envelope.getCompletedDateTime());
                        processed.setUploadStatus(UploadStatus.UPLOADED);
                        processed.setProcessingTime(Duration.between(processingStart, Instant.now()));

                        log.info(
                                        "Successfully archived envelope '{}'.", envelope.getEnvelopeId());
                        return processed;
                } catch (Exception exception) {

                        log.error("Failed to process envelope '{}'.", envelope.getEnvelopeId(), exception);
                        throw new DocumentSyncException(
                                        "Failed to process Envelope: " + envelope.getEnvelopeId(),
                                        exception);
                }
        }

        private String generateObjectName(
                        EnvelopeInfo envelope,
                        Instant tenantSigningDate) {

                String subject = envelope.getEmailSubject();

                if (subject == null || subject.isBlank()) {
                        subject = "Document";
                }

                String sanitizedSubject = subject
                                .replaceAll("[\\\\/:*?\"<>|]", "")
                                .replaceAll("\\s+", "_")
                                .trim();

                String date = java.time.LocalDate.ofInstant(
                                tenantSigningDate,
                                java.time.ZoneOffset.UTC)
                                .toString();

                String shortId = envelope.getEnvelopeId().substring(0, 8);

                return date + "_" + sanitizedSubject + "_" + shortId + ".pdf";
        }
}