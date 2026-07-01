package com.accenture.document_sync_service.service.impl;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.config.GoogleCloudStorageProperties;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.service.UploadService;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCloudStorageUploadService
        implements UploadService {

    private final Storage storage;
    private final GoogleCloudStorageProperties properties;

    @Override
    public void upload(
            String objectName,
            InputStream inputStream,
            String contentType) {

        if (inputStream == null) {
            throw new IllegalArgumentException(
                    "InputStream cannot be null."
            );
        }

        if (objectName == null || objectName.isBlank()) {
            throw new IllegalArgumentException(
                    "Object name cannot be null or blank."
            );
        }

        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException(
                    "Content type cannot be null or blank."
            );
        }

        log.info(
                "Uploading object '{}' to bucket '{}'.",
                objectName,
                properties.getBucketName()
        );

        try {

            BlobInfo blobInfo = BlobInfo.newBuilder(
                            properties.getBucketName(),
                            objectName)
                    .setContentType(contentType)
                    .build();

            storage.createFrom(
                    blobInfo,
                    inputStream
            );

            log.info(
                    "Successfully uploaded '{}' to bucket '{}'.",
                    objectName,
                    properties.getBucketName()
            );

        } catch (Exception exception) {

            log.error(
                    "Failed to upload '{}' to Google Cloud Storage.",
                    objectName,
                    exception
            );

            throw new DocumentSyncException(
                    "Unable to upload object to Google Cloud Storage.",
                    exception
            );
        }
    }
}