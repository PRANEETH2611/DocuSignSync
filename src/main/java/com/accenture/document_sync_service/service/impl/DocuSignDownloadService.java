package com.accenture.document_sync_service.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

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
    private final HttpClient httpClient;
    private final DocusignProperties properties;

    @Override
    public InputStream downloadCompletedDocument(String envelopeId) {

        log.info(
                "Downloading completed document for envelope '{}'.",
                envelopeId);

        try {

            AccessToken accessToken =
                    authenticationService.getAccessToken();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(
                            URI.create(
                                    properties.getBaseUrl()
                                            + "/v2.1/accounts/"
                                            + properties.getAccountId()
                                            + "/envelopes/"
                                            + envelopeId
                                            + "/documents/combined"))
                    .header(
                            "Authorization",
                            "Bearer " + accessToken.value())
                    .GET()
                    .build();

            HttpResponse<InputStream> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {

                throw new DocumentSyncException(
                        "DocuSign returned HTTP status "
                                + response.statusCode());
            }

            InputStream inputStream = response.body();

            if (inputStream == null) {

                throw new DocumentSyncException(
                        "Downloaded document stream is empty.");
            }

            log.info(
                    "Successfully downloaded completed document for envelope '{}'.",
                    envelopeId);

            return inputStream;

        } catch (DocumentSyncException exception) {

            throw exception;

        } catch (IOException exception) {

            log.error(
                    "Failed to download completed document.",
                    exception);

            throw new DocumentSyncException(
                    "Unable to download completed document.",
                    exception);

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            log.error(
                    "Download operation was interrupted.",
                    exception);

            throw new DocumentSyncException(
                    "Download operation was interrupted.",
                    exception);
        }
    }
}