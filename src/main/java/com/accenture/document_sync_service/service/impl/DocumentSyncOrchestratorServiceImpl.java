package com.accenture.document_sync_service.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.config.GoogleCloudStorageProperties;
import com.accenture.document_sync_service.notification.enums.EnvironmentType;
import com.accenture.document_sync_service.notification.enums.FailureStage;
import com.accenture.document_sync_service.notification.enums.JobStatus;
import com.accenture.document_sync_service.notification.enums.TriggerType;
import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.model.FailureSummary;
import com.accenture.document_sync_service.notification.service.EmailService;
import com.accenture.document_sync_service.service.DocumentSyncOrchestratorService;
import com.accenture.document_sync_service.service.PollingWorkflowService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentSyncOrchestratorServiceImpl
        implements DocumentSyncOrchestratorService {

    private final PollingWorkflowService pollingWorkflowService;
    private final EmailService emailService;
    private final GoogleCloudStorageProperties storageProperties;

    @Override
    public void synchronizeCompletedDocuments() {

        log.info("==============================================");
        log.info("Starting Document Synchronization");
        log.info("==============================================");

        try {

            ExecutionSummary summary = pollingWorkflowService.executePollingWorkflow();

            emailService.sendSuccessEmail(summary);

            log.info("Success notification email sent.");

        } catch (Exception exception) {

            log.error("Document synchronization failed.", exception);

            ExecutionSummary summary = buildFailureSummary(exception);

            emailService.sendFailureEmail(summary);

            log.info("Failure notification email sent.");
        }

        log.info("==============================================");
        log.info("Document Synchronization Finished");
        log.info("==============================================");
    }

    private ExecutionSummary buildFailureSummary(Exception exception) {

        Instant now = Instant.now();

        FailureSummary failureSummary = new FailureSummary();
        failureSummary.setFailureTime(now);
        failureSummary.setExceptionType(exception.getClass().getSimpleName());
        failureSummary.setErrorMessage(exception.getMessage());

        ExecutionSummary summary = new ExecutionSummary();
        summary.setStatus(JobStatus.FAILURE);
        summary.setEnvironment(EnvironmentType.DEV);
        failureSummary.setStage(FailureStage.ORCHESTRATION);
        summary.setTrigger(TriggerType.CLOUD_SCHEDULER);
        summary.setBucketName(storageProperties.getBucketName());

        summary.setStartedAt(now);
        summary.setCompletedAt(now);
        summary.setDuration(Duration.ZERO);
        summary.setFailureSummary(failureSummary);

        return summary;
    }
}