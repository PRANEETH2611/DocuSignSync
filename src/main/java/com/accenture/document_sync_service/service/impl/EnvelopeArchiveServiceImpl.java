package com.accenture.document_sync_service.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.entity.EnvelopeArchive;
import com.accenture.document_sync_service.repository.EnvelopeArchiveRepository;
import com.accenture.document_sync_service.service.EnvelopeArchiveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvelopeArchiveServiceImpl
        implements EnvelopeArchiveService {

    private final EnvelopeArchiveRepository repository;

    @Override
    public boolean isArchived(String envelopeId) {

        return repository.existsById(envelopeId);
    }

    @Override
    public void archive(
            String envelopeId,
            String objectName) {

        EnvelopeArchive archive = EnvelopeArchive.builder()
                .envelopeId(envelopeId)
                .objectName(objectName)
                .archivedAt(Instant.now())
                .build();

        repository.save(archive);
    }
}