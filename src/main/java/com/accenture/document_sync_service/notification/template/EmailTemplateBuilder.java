package com.accenture.document_sync_service.notification.template;

import com.accenture.document_sync_service.notification.model.ExecutionSummary;

public interface EmailTemplateBuilder {

    String build(ExecutionSummary executionSummary);
    

}