package com.accenture.document_sync_service.service;

import java.time.Instant;

import com.accenture.document_sync_service.entity.SchedulerCheckpoint;

public interface CheckpointService {

        SchedulerCheckpoint getCheckpoint();

        void updateCheckpoint(
        Instant scheduledRunTime);

        void updateRunStatus(
                        String status,
                        long durationMs);
}