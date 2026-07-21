package com.accenture.document_sync_service.service;

import com.accenture.document_sync_service.notification.model.ExecutionSummary;

public interface PollingWorkflowService {

    ExecutionSummary executePollingWorkflow();

}