package com.accenture.document_sync_service.notification.template;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.accenture.document_sync_service.notification.enums.EnvironmentType;
import com.accenture.document_sync_service.notification.enums.JobStatus;
import com.accenture.document_sync_service.notification.enums.TriggerType;
import com.accenture.document_sync_service.notification.enums.UploadStatus;
import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.model.ProcessedEnvelopeSummary;

class SuccessEmailTemplateBuilderTest {

    private SuccessEmailTemplateBuilder builder;

    @BeforeEach
    void setUp() {

        builder = new SuccessEmailTemplateBuilder();
    }

    @Test
    void shouldBuildSuccessEmail() {

        ExecutionSummary summary = createExecutionSummary();

        String html = builder.build(summary);

        assertTrue(html.contains("EXECUTION COMPLETED SUCCESSFULLY"));
        assertTrue(html.contains("Processing Statistics"));
        assertTrue(html.contains("Checkpoint Information"));
        assertTrue(html.contains("Google Cloud Storage"));
        assertTrue(html.contains("Processed Documents"));
    }

    private ExecutionSummary createExecutionSummary() {

        ExecutionSummary summary = new ExecutionSummary();

        summary.setJobId("JOB-001");
        summary.setStatus(JobStatus.SUCCESS);
        summary.setEnvironment(EnvironmentType.LOCAL);
        summary.setTrigger(TriggerType.MANUAL);

        summary.setStartedAt(Instant.now().minusSeconds(20));
        summary.setCompletedAt(Instant.now());

        summary.setDuration(Duration.ofSeconds(20));

        summary.setEnvelopesFound(5);
        summary.setUploaded(4);
        summary.setSkipped(1);
        summary.setFailed(0);

        summary.setPreviousCheckpoint(Instant.now().minusSeconds(3600));
        summary.setNewCheckpoint(Instant.now());

        summary.setBucketName("document-sync");
        summary.setBucketFolder("completed");

        summary.setProcessedEnvelopes(Collections.emptyList());

        return summary;
    }

    @Test
    void shouldDisplayNoDocumentsMessage() {

        ExecutionSummary summary = createExecutionSummary();

        summary.setProcessedEnvelopes(Collections.emptyList());

        String html = builder.build(summary);

        assertTrue(html.contains("No documents were processed."));
    }

    @Test
    void shouldDisplayProcessedDocuments() {

        ExecutionSummary summary = createExecutionSummary();

        ProcessedEnvelopeSummary envelope = new ProcessedEnvelopeSummary();

        envelope.setEnvelopeId("ENV-001");
        envelope.setEnvelopeSubject("Employment Agreement");
        envelope.setDocumentName("agreement.pdf");
        envelope.setCompletedAt(Instant.now());
        envelope.setFileSize(2048L);
        envelope.setUploadStatus(UploadStatus.UPLOADED);

        summary.setProcessedEnvelopes(List.of(envelope));

        String html = builder.build(summary);

        assertTrue(html.contains("ENV-001"));
        assertTrue(html.contains("Employment Agreement"));
        assertTrue(html.contains("agreement.pdf"));
        assertTrue(html.contains("SUCCESS"));
    }
}