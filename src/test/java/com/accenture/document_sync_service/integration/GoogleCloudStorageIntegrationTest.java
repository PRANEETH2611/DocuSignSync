package com.accenture.document_sync_service.integration;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleCloudStorageIntegrationTest {

    @Autowired
    private Storage storage;

    @Autowired
    private com.accenture.document_sync_service.config.GoogleCloudStorageProperties properties;

    @Test
    void shouldConnectToBucket() {

        Bucket bucket = storage.get(properties.getBucketName());

        assertNotNull(bucket);
        assertEquals(
                properties.getBucketName(),
                bucket.getName()
        );
    }
}