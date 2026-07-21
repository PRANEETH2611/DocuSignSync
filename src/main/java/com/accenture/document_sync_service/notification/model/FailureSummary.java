package com.accenture.document_sync_service.notification.model;

import com.accenture.document_sync_service.notification.enums.FailureStage;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class FailureSummary {

    private FailureStage stage;

    private Instant failureTime;

    private String envelopeId;

    private String envelopeSubject;

    private String documentName;

    private String errorMessage;

    private String exceptionType;

    private Integer httpStatus;

    private Integer retryAttempts;

}