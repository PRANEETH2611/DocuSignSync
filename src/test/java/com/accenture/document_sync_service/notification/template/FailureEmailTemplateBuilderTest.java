package com.accenture.document_sync_service.notification.template;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.accenture.document_sync_service.notification.enums.EnvironmentType;
import com.accenture.document_sync_service.notification.enums.FailureStage;
import com.accenture.document_sync_service.notification.enums.JobStatus;
import com.accenture.document_sync_service.notification.enums.TriggerType;
import com.accenture.document_sync_service.notification.model.ExecutionSummary;
import com.accenture.document_sync_service.notification.model.FailureSummary;

class FailureEmailTemplateBuilderTest {

    private FailureEmailTemplateBuilder builder;

    @BeforeEach
    void setUp() {

        builder = new FailureEmailTemplateBuilder();
    }

    @Test
    void shouldBuildFailureEmail() {

        ExecutionSummary summary = createExecutionSummary();

        String html = builder.build(summary);

        assertTrue(html.contains("EXECUTION FAILED"));
        assertTrue(html.contains("Failure Details"));
        assertTrue(html.contains("Suggested Actions"));
    }
        @Test
    void shouldDisplayFailureDetails() {

        ExecutionSummary summary = createExecutionSummary();

        String html = builder.build(summary);

        assertTrue(html.contains("DOWNLOAD"));
        assertTrue(html.contains("RuntimeException"));
        assertTrue(html.contains("Network timeout"));
        assertTrue(html.contains("500"));
        assertTrue(html.contains("3"));
    }
        @Test
    void shouldDisplaySuggestedActions() {

        ExecutionSummary summary = createExecutionSummary();

        String html = builder.build(summary);

        assertTrue(html.contains("Verify DocuSign connectivity"));
        assertTrue(html.contains("Review application logs"));
        assertTrue(html.contains("Google Cloud Storage"));
        assertTrue(html.contains("Retry the scheduled execution"));
    }
        private ExecutionSummary createExecutionSummary() {

        ExecutionSummary summary = new ExecutionSummary();

        summary.setJobId("JOB-001");

        summary.setStatus(JobStatus.FAILURE);

        summary.setEnvironment(EnvironmentType.LOCAL);

        summary.setTrigger(TriggerType.MANUAL);

        summary.setStartedAt(Instant.now().minusSeconds(60));

        summary.setCompletedAt(Instant.now());

        summary.setDuration(Duration.ofSeconds(60));

        FailureSummary failure = new FailureSummary();

        failure.setStage(FailureStage.DOWNLOAD);

        failure.setExceptionType("RuntimeException");

        failure.setErrorMessage("Network timeout");

        failure.setHttpStatus(500);

        failure.setRetryAttempts(3);

        summary.setFailureSummary(failure);

        return summary;
    }

}