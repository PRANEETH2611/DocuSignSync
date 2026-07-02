package com.accenture.document_sync_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accenture.document_sync_service.entity.ArchiveJob;
import com.accenture.document_sync_service.enums.ArchiveStatus;

public interface ArchiveJobRepository
        extends MongoRepository<ArchiveJob, String> {

    List<ArchiveJob> findByArchiveStatusOrderByCreatedAtAsc(
            ArchiveStatus archiveStatus);

}