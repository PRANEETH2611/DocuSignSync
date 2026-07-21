package com.accenture.document_sync_service.service;

import java.util.List;

import com.accenture.document_sync_service.dto.docusign.envelope.EnvelopeInfo;
import com.accenture.document_sync_service.entity.SchedulerCheckpoint;

public interface EnvelopePollingService {

    List<EnvelopeInfo> getCompletedEnvelopes(
            SchedulerCheckpoint checkpoint);
}