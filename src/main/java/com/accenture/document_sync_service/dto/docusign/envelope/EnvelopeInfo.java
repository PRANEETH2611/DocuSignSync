package com.accenture.document_sync_service.dto.docusign.envelope;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvelopeInfo {

    private String envelopeId;

    private String emailSubject;

    private String status;

    private Instant completedDateTime;
}