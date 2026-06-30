package com.accenture.document_sync_service.client;

import org.springframework.stereotype.Component;

import com.accenture.document_sync_service.logging.LoggingConstants;
import com.accenture.document_sync_service.model.AuthenticationToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocuSignSdkClient implements DocuSignClient {

    @Override
    public AuthenticationToken authenticate() {

        log.info(LoggingConstants.AUTH_START);

        throw new UnsupportedOperationException(
                "Authentication implementation will be added next.");
    }
}