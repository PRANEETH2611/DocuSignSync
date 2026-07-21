package com.accenture.document_sync_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.document_sync_service.service.DocumentSyncOrchestratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/document-sync")
@RequiredArgsConstructor
public class DocumentSyncController {

    private final DocumentSyncOrchestratorService documentSyncOrchestratorService;

    @PostMapping("/run")
    public ResponseEntity<Void> runDocumentSync() {

        log.info("Received document synchronization request from Cloud Scheduler.");

        documentSyncOrchestratorService.synchronizeCompletedDocuments();

        return ResponseEntity.accepted().build();
    }
}