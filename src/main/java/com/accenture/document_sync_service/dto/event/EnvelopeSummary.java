package com.accenture.document_sync_service.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvelopeSummary {

    private String status;

}