package com.accenture.document_sync_service.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.dto.AccessToken;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.service.AuthenticationService;
import com.accenture.document_sync_service.service.DocuSignRestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocuSignRestClientImpl implements DocuSignRestClient {

    private final AuthenticationService authenticationService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Override
    public <T> T get(URI uri, Class<T> responseType) {

        try {

            HttpRequest request = createAuthenticatedGetRequest(uri);

            log.debug("Executing GET request to DocuSign URI: {}", uri);

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            validateResponse(
                    response.statusCode(),
                    response.body());

            return deserialize(
                    response.body(),
                    responseType);

        } catch (IOException e) {

            throw new DocumentSyncException(
                    "Failed to execute GET request to DocuSign: " + uri,
                    e);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new DocumentSyncException(
                    "DocuSign GET request interrupted: " + uri,
                    e);
        }
    }

    @Override
    public InputStream download(URI uri) {

        try {

            HttpRequest request = createAuthenticatedGetRequest(uri);

            log.debug("Downloading document from DocuSign URI: {}", uri);

            HttpResponse<InputStream> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofInputStream());

            validateResponse(
                    response.statusCode(),
                    "");

            return response.body();

        } catch (IOException e) {

            throw new DocumentSyncException(
                    "Failed to download document from DocuSign: " + uri,
                    e);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new DocumentSyncException(
                    "DocuSign download interrupted: " + uri,
                    e);
        }
    }

    private HttpRequest createAuthenticatedGetRequest(URI uri) {

        AccessToken accessToken = authenticationService.getAccessToken();

        return createGetRequest(
                uri,
                accessToken.value());
    }

    private HttpRequest createGetRequest(URI uri, String accessToken) {

        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .GET()
                .build();
    }

    private void validateResponse(
            int statusCode,
            String responseBody) {

        if (statusCode >= 200 && statusCode < 300) {
            return;
        }

        throw new DocumentSyncException(
                String.format(
                        "DocuSign API request failed. Status: %d, Response: %s",
                        statusCode,
                        responseBody));
    }

    private <T> T deserialize(
            String responseBody,
            Class<T> responseType) {

        try {
            return objectMapper.readValue(
                    responseBody,
                    responseType);

        } catch (IOException e) {

            throw new DocumentSyncException(
                    "Failed to deserialize DocuSign response.",
                    e);
        }
    }
}