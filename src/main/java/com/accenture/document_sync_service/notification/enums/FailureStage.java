package com.accenture.document_sync_service.notification.enums;

public enum FailureStage {

    AUTHENTICATION,

    POLLING,

    DOWNLOAD,

    ORCHESTRATION,
    
    UPLOAD,

    CHECKPOINT_UPDATE,

    EMAIL_NOTIFICATION,

    UNKNOWN

}