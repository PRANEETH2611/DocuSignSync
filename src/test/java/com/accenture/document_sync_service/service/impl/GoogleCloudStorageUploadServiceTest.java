package com.accenture.document_sync_service.service.impl;

import com.accenture.document_sync_service.config.GoogleCloudStorageProperties;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class GoogleCloudStorageUploadServiceTest {

    @Test
    void shouldThrowExceptionWhenInputStreamIsNull() {

        Storage storage = mock(Storage.class);

        GoogleCloudStorageProperties properties =
                new GoogleCloudStorageProperties();

        properties.setBucketName("test-bucket");

        GoogleCloudStorageUploadService service =
                new GoogleCloudStorageUploadService(
                        storage,
                        properties
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.upload(
                        "test.pdf",
                        null,
                        "application/pdf"
                )
        );
    }
}