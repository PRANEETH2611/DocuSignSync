package com.accenture.document_sync_service.service;

import java.util.List;

import com.accenture.document_sync_service.entity.ArchiveJob;
import com.accenture.document_sync_service.enums.ArchiveStatus;

public interface ArchiveJobService {

    ArchiveJob savePendingJob(String envelopeId);

    List<ArchiveJob> findPendingJobs();

    void updateStatus(
        String id,
        ArchiveStatus archiveStatus
    );

}