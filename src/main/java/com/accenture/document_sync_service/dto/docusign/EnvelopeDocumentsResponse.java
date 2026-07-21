package com.accenture.document_sync_service.dto.docusign;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvelopeDocumentsResponse {

    private List<EnvelopeDocument> envelopeDocuments;
}