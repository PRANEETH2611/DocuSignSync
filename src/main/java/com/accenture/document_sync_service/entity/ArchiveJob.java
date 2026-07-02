package com.accenture.document_sync_service.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.accenture.document_sync_service.enums.ArchiveStatus;


import lombok.Builder;  
import lombok.Data;

@Data
@Builder
@Document(collection = "archive_jobs")
public class ArchiveJob {

    @Id
    private String id;

    private String envelopeId;

    private ArchiveStatus archiveStatus;

    @Builder.Default
    private int retryCount=0;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant lastUpdatedAt;

}