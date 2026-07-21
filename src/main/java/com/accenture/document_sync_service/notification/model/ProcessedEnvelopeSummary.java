package com.accenture.document_sync_service.notification.model;

import com.accenture.document_sync_service.notification.enums.UploadStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
public class ProcessedEnvelopeSummary {

    private String envelopeId;

    private String envelopeSubject;

    private String documentName;

    private Instant completedAt;

    private long fileSize;

    private Duration processingTime;

    private String gcsObjectPath;

    private UploadStatus uploadStatus;

}