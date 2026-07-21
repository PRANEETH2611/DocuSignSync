package com.accenture.document_sync_service.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class GoogleCloudStoragePropertiesTest {

        @Test
        void shouldStorePropertiesCorrectly() {

                GoogleCloudStorageProperties properties = new GoogleCloudStorageProperties();

                properties.setProjectId("project-id");
                properties.setBucketName("bucket-name");
                // properties.setCredentialsPath("credentials.json");

                assertEquals(
                                "project-id",
                                properties.getProjectId());

                assertEquals(
                                "bucket-name",
                                properties.getBucketName());

                // assertEquals(
                // "credentials.json",
                // properties.getCredentialsPath()
                // );
        }
}