package com.accenture.document_sync_service.service;

import java.time.Instant;
import java.util.List;

import com.accenture.document_sync_service.dto.docusign.envelope.EnvelopeInfo;

public interface EnvelopePollingService {

   List<EnvelopeInfo> getCompletedEnvelopes(
        Instant fromTime,
        Instant toTime);
}