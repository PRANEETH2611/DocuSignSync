package com.accenture.document_sync_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accenture.document_sync_service.entity.SchedulerCheckpoint;

public interface SchedulerCheckpointRepository
        extends MongoRepository<SchedulerCheckpoint, String> {
}