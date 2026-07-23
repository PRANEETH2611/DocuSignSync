package com.accenture.document_sync_service.service.impl;

import java.net.URI;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.dto.docusign.recipient.RecipientResponse;
import com.accenture.document_sync_service.dto.docusign.recipient.Signer;
import com.accenture.document_sync_service.service.DocuSignRestClient;
import com.accenture.document_sync_service.service.RecipientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipientServiceImpl
                implements RecipientService {

        private final DocuSignRestClient docuSignRestClient;
        private final DocusignProperties properties;

        @Override
        public Instant getTenantSignedDate(String envelopeId) {

                URI uri = URI.create(
                                String.format(
                                                "%s/v2.1/accounts/%s/envelopes/%s/recipients",
                                                properties.getBaseUrl(),
                                                properties.getAccountId(),
                                                envelopeId));

                RecipientResponse response = docuSignRestClient.get(
                                uri,
                                RecipientResponse.class);

                for (Signer signer : response.getSigners()) {

                        log.info(
                                        "Signer -> name={}, routingOrder={}, status={}, completedDateTime={}",
                                        signer.getName(),
                                        signer.getRoutingOrder(),
                                        signer.getStatus(),
                                        signer.getSignedDateTime());
                }

                Signer tenant = response.getSigners()
                                .stream()
                                .filter(signer -> "1".equals(signer.getRoutingOrder()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(
                                                "Tenant recipient not found."));

                if (tenant.getSignedDateTime() == null) {
                throw new IllegalStateException(
                "Tenant completedDateTime is null.");
                }

                return tenant.getSignedDateTime();
        }
}