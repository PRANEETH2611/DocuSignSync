package com.accenture.document_sync_service.dto.docusign.envelope;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvelopeListResponse {

    private List<EnvelopeInfo> envelopes;

    private String nextUri;

    private String previousUri;

    private Integer resultSetSize;

    private Integer totalSetSize;

    private Integer startPosition;

    private Integer endPosition;
}
