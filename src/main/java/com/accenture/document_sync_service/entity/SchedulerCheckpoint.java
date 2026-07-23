package com.accenture.document_sync_service.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "scheduler_checkpoint")
public class SchedulerCheckpoint {

    @Id
    private String id;

    /**
     * Last successful scheduler execution start time.
     */
    private Instant lastScheduledRunTime;

    /**
     * Last scheduler execution time.
     */
    private Instant lastRunTime;

    /**
     * SUCCESS / FAILURE
     */
    private String lastRunStatus;

    /**
     * Time taken in milliseconds.
     */
    private Long lastRunDurationMs;
}