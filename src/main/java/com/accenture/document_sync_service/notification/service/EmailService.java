package com.accenture.document_sync_service.notification.service;

import com.accenture.document_sync_service.notification.model.ExecutionSummary;

public interface EmailService {


    void sendSuccessEmail(ExecutionSummary executionSummary);

    void sendFailureEmail(ExecutionSummary executionSummary);

}