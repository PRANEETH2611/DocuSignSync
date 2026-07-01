package com.accenture.document_sync_service.service;

import java.io.InputStream;

public interface DownloadService {

    InputStream downloadCompletedDocument(String envelopeId);

}