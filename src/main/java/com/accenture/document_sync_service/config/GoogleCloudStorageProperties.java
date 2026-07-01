package com.accenture.document_sync_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Validated
@ConfigurationProperties(prefix = "gcp.storage")
public class GoogleCloudStorageProperties {

    @NotBlank
    private String projectId;

    @NotBlank
    private String bucketName;

    @NotBlank
    private String credentialsPath;

}