package com.accenture.document_sync_service.service.impl;

import java.time.Clock;
import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.dto.AccessToken;
import com.accenture.document_sync_service.dto.AuthenticationResponse;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.security.jwt.JwtGenerator;
import com.accenture.document_sync_service.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocuSignAuthenticationService implements AuthenticationService {

    private final JwtGenerator jwtGenerator;
    private final RestClient restClient;
    private final DocusignProperties properties;
    private final Clock clock;

    @Override
    public AccessToken getAccessToken() {

        log.info("Authenticating with DocuSign.");

        try {

            String jwt = jwtGenerator.generateJwt();

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add(
                    "grant_type",
                    "urn:ietf:params:oauth:grant-type:jwt-bearer"
            );
            form.add("assertion", jwt);

            AuthenticationResponse response = restClient.post()
                    .uri(properties.getOauthBaseUrl() + "/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(AuthenticationResponse.class);

            if (response == null || response.getAccessToken() == null) {
                throw new DocumentSyncException(
                        "DocuSign authentication returned an empty response."
                );
            }

            log.info("Successfully authenticated with DocuSign.");

            return new AccessToken(
                    response.getAccessToken(),
                    Instant.now(clock).plusSeconds(response.getExpiresIn())
            );

        } catch (DocumentSyncException exception) {

            throw exception;

        } catch (Exception exception) {

            log.error("DocuSign authentication failed.", exception);

            throw new DocumentSyncException(
                    "Unable to authenticate with DocuSign.",
                    exception
            );
        }
    }
}