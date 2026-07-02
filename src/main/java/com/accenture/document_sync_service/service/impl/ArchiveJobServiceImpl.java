package com.accenture.document_sync_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.entity.ArchiveJob;
import com.accenture.document_sync_service.enums.ArchiveStatus;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.repository.ArchiveJobRepository;
import com.accenture.document_sync_service.service.ArchiveJobService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArchiveJobServiceImpl implements ArchiveJobService {

    private final ArchiveJobRepository repository;

    @Override
    public ArchiveJob savePendingJob(String envelopeId) {

        ArchiveJob job = ArchiveJob.builder()
                .envelopeId(envelopeId)
                .archiveStatus(ArchiveStatus.PENDING)
                .build();

        return repository.save(job);
    }

   @Override
public List<ArchiveJob> findPendingJobs() {

    return repository.findByArchiveStatusOrderByCreatedAtAsc(
            ArchiveStatus.PENDING);
}

    @Override
public void updateStatus(
        String id,
        ArchiveStatus archiveStatus) {

    ArchiveJob job =
            repository.findById(id)
                    .orElseThrow(() ->
                            new DocumentSyncException(
                                    "Archive job not found: " + id));

    job.setArchiveStatus(archiveStatus);

    repository.save(job);
}
}