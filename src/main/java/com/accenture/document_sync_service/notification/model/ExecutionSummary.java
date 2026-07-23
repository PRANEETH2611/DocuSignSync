package com.accenture.document_sync_service.notification.model;

import com.accenture.document_sync_service.notification.enums.EnvironmentType;
import com.accenture.document_sync_service.notification.enums.JobStatus;
import com.accenture.document_sync_service.notification.enums.TriggerType;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ExecutionSummary {

    // Job Information
    private String jobId;

    private JobStatus status;

    private EnvironmentType environment;

    private TriggerType trigger;

    private Instant startedAt;

    private Instant completedAt;

    private Duration duration;

    public String getFormattedDuration() {

        return String.format("%.2f seconds",
                duration.toMillis() / 1000.0);

    }

    // Processing Statistics
    private int envelopesFound;

    private int uploaded;

    private int skipped;

    private int failed;

    // Checkpoint Information
    private Instant previousCheckpoint;

    private Instant newCheckpoint;

    // Storage Information
    private String bucketName;

    private String bucketFolder;

    // Envelope Details
    private List<ProcessedEnvelopeSummary> processedEnvelopes;

    // Failure Details (null for successful executions)
    private FailureSummary failureSummary;

}