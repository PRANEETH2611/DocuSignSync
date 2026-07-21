package com.accenture.document_sync_service.dto;

import java.time.Instant;
import java.util.List;

import com.accenture.document_sync_service.notification.model.ProcessedEnvelopeSummary;

public class PollingWorkflowResult {

    private int envelopesFound;
    private int uploaded;
    private int failed;
    private Instant previousCheckpoint;
    private Instant newCheckpoint;
    private List<ProcessedEnvelopeSummary> processedEnvelopes;
}