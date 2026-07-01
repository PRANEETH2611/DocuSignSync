package com.accenture.document_sync_service.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GoogleCloudStorageConfiguration {

    private final GoogleCloudStorageProperties properties;

    @Bean
    public Storage storage() throws IOException {

        GoogleCredentials credentials =
                GoogleCredentials.fromStream(
                        new FileInputStream(
                                properties.getCredentialsPath()
                        )
                );

        return StorageOptions.newBuilder()
                .setProjectId(properties.getProjectId())
                .setCredentials(credentials)
                .build()
                .getService();
    }
}