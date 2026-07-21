package com.accenture.document_sync_service.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.entity.SchedulerCheckpoint;
import com.accenture.document_sync_service.repository.SchedulerCheckpointRepository;
import com.accenture.document_sync_service.service.CheckpointService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckpointServiceImpl
                implements CheckpointService {

        private static final String CHECKPOINT_ID = "scheduler";
        private static final Instant INITIAL_CHECKPOINT =
        Instant.parse("2000-01-01T00:00:00Z");
        private final SchedulerCheckpointRepository repository;

        @Override
        public SchedulerCheckpoint getCheckpoint() {

                return repository.findById(CHECKPOINT_ID)
                                .orElseGet(this::createInitialCheckpoint);
        }

        @Override
        public void updateCheckpoint(
                        Instant completedDate,
                        String envelopeId) {

                SchedulerCheckpoint checkpoint = getCheckpoint();

                checkpoint.setLastProcessedCompletedDate(
                                completedDate);

                checkpoint.setLastProcessedEnvelopeId(
                                envelopeId);

                repository.save(checkpoint);
        }

        @Override
        public void updateRunStatus(
                        String status,
                        long durationMs) {

                SchedulerCheckpoint checkpoint = getCheckpoint();

                checkpoint.setLastRunStatus(status);

                checkpoint.setLastRunTime(
                                Instant.now());

                checkpoint.setLastRunDurationMs(
                                durationMs);

                repository.save(checkpoint);
        }

        private SchedulerCheckpoint createInitialCheckpoint() {

                SchedulerCheckpoint checkpoint = SchedulerCheckpoint.builder()
                                .id(CHECKPOINT_ID)
                                .lastProcessedCompletedDate(INITIAL_CHECKPOINT)
                                .build();

                return repository.save(checkpoint);
        }
}