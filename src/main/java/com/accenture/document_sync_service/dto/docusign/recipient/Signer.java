package com.accenture.document_sync_service.dto.docusign.recipient;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Signer {

    @JsonProperty("routingOrder")
    private String routingOrder;

    @JsonProperty("SignedDateTime")
    private Instant SignedDateTime;

    private String status;

    private String name;
}