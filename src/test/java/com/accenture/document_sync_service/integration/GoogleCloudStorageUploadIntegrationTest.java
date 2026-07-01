package com.accenture.document_sync_service.integration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.accenture.document_sync_service.config.GoogleCloudStorageProperties;
import com.accenture.document_sync_service.service.UploadService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;

@SpringBootTest
class GoogleCloudStorageUploadIntegrationTest {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private Storage storage;

    @Autowired
    private GoogleCloudStorageProperties properties;

    @Test
    void shouldUploadAndDeleteObject() {

        String objectName = "integration-test-file.txt";

        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(
                        "Hello Google Cloud Storage!"
                                .getBytes(StandardCharsets.UTF_8));

        uploadService.upload(
                objectName,
                inputStream,
                "text/plain");

        Blob blob = storage.get(
                properties.getBucketName(),
                objectName);

        assertNotNull(blob);

        assertTrue(storage.delete(
                properties.getBucketName(),
                objectName));
    }
}