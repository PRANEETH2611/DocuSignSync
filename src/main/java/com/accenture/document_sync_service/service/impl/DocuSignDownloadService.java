package com.accenture.document_sync_service.service.impl;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.dto.AccessToken;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.service.AuthenticationService;
import com.accenture.document_sync_service.service.DownloadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocuSignDownloadService implements DownloadService {

    private final AuthenticationService authenticationService;
    private final RestClient restClient;
    private final DocusignProperties properties;

   @Override
public InputStream downloadCompletedDocument(String envelopeId) {

    log.info("Downloading completed document for envelope {}.", envelopeId);

    try {

        AccessToken accessToken = authenticationService.getAccessToken();

        InputStream inputStream = restClient.get()
                .uri(properties.getBaseUrl()
                        + "/v2.1/accounts/"
                        + properties.getAccountId()
                        + "/envelopes/"
                        + envelopeId
                        + "/documents/combined")
                .headers(headers ->
                        headers.setBearerAuth(accessToken.value()))
                .retrieve()
                .body(InputStream.class);

        if (inputStream == null) {
            throw new DocumentSyncException(
                    "Downloaded document stream is empty."
            );
        }

        log.info("Document downloaded successfully.");

        return inputStream;

    } catch (DocumentSyncException exception) {

        throw exception;

    } catch (Exception exception) {

        log.error("Failed to download completed document.", exception);

        throw new DocumentSyncException(
                "Unable to download completed document.",
                exception
        );
    }
}
}