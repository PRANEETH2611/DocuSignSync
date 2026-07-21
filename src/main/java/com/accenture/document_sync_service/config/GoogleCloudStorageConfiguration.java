package com.accenture.document_sync_service.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GoogleCloudStorageConfiguration {

        private final GoogleCloudStorageProperties properties;

        @Bean
        public Storage storage() {

                try {

                        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

                        return StorageOptions.newBuilder()
                                        .setProjectId(properties.getProjectId())
                                        .setCredentials(credentials)
                                        .build()
                                        .getService();

                } catch (IOException exception) {

                        log.error("Unable to initialize Google Cloud Storage.", exception);

                        throw new DocumentSyncException(
                                        "Failed to initialize Google Cloud Storage.",
                                        exception);
                }
        }
}