package com.accenture.document_sync_service.service;

public interface SchedulerLockService {

    boolean acquireLock();

    void releaseLock();

}