package com.accenture.document_sync_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accenture.document_sync_service.entity.EnvelopeArchive;

public interface EnvelopeArchiveRepository
        extends MongoRepository<EnvelopeArchive, String> {
}