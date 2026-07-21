package com.accenture.document_sync_service.dto.docusign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvelopeDocument {

    private String documentId;

    private String name;

    private String type;
}