package com.accenture.document_sync_service.service;

import java.io.InputStream;

public interface UploadService {

    void upload(
            String objectName,
            InputStream inputStream,
            String contentType
    );

}