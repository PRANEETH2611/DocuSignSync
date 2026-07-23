package com.accenture.document_sync_service.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "envelope_archive")
public class EnvelopeArchive {

    @Id
    private String envelopeId;

    private String objectName;

    private Instant archivedAt;
}