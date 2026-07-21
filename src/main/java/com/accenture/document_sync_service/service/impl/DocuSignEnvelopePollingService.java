package com.accenture.document_sync_service.service.impl;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.dto.docusign.envelope.EnvelopeInfo;
import com.accenture.document_sync_service.dto.docusign.envelope.EnvelopeListResponse;
import com.accenture.document_sync_service.entity.SchedulerCheckpoint;
import com.accenture.document_sync_service.service.DocuSignRestClient;
import com.accenture.document_sync_service.service.EnvelopePollingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocuSignEnvelopePollingService
                implements EnvelopePollingService {

        private static final int PAGE_SIZE = 100;

        private final DocuSignRestClient docuSignRestClient;
        private final DocusignProperties properties;

        @Override
        public List<EnvelopeInfo> getCompletedEnvelopes(
                        SchedulerCheckpoint checkpoint) {

                Instant fromDate = checkpoint.getLastProcessedCompletedDate();
                String lastEnvelopeId = checkpoint.getLastProcessedEnvelopeId();

                log.info("Polling completed envelopes after '{}'.", fromDate);

                List<EnvelopeInfo> envelopes = new ArrayList<>();

                int startPosition = 0;

                while (true) {

                        EnvelopeListResponse response = fetchPage(
                                        fromDate,
                                        startPosition);

                        if (response.getEnvelopes() == null
                                        || response.getEnvelopes().isEmpty()) {
                                break;
                        }

                        envelopes.addAll(response.getEnvelopes());

                        log.info(
                                        "Fetched {} envelopes. Total collected: {}",
                                        response.getEnvelopes().size(),
                                        envelopes.size());

                        if (response.getNextUri() == null
                                        || response.getNextUri().isBlank()) {
                                break;
                        }

                        startPosition = response.getEndPosition() + 1;
                }

                return envelopes.stream()
                                .filter(this::isCompletedEnvelope)
                                .sorted(
                                                Comparator.comparing(EnvelopeInfo::getCompletedDateTime)
                                                                .thenComparing(EnvelopeInfo::getEnvelopeId))
                                .filter(envelope -> {

                                        if (fromDate == null) {
                                                return true;
                                        }

                                        if (lastEnvelopeId == null) {
                                                return true;
                                        }

                                        return !(fromDate.equals(envelope.getCompletedDateTime())
                                                        && lastEnvelopeId.equals(envelope.getEnvelopeId()));
                                })
                                .toList();
        }

        private EnvelopeListResponse fetchPage(
                        Instant checkpoint,
                        int startPosition) {

                URI uri = buildPollingUri(
                                checkpoint,
                                startPosition);

                log.debug(
                                "Fetching DocuSign envelopes. Start Position: {}",
                                startPosition);

                return docuSignRestClient.get(
                                uri,
                                EnvelopeListResponse.class);
        }

        private URI buildPollingUri(
                        Instant checkpoint,
                        int startPosition) {

                String fromDate = URLEncoder.encode(
                                DateTimeFormatter.ISO_INSTANT.format(checkpoint),
                                StandardCharsets.UTF_8);

                String url = String.format(
                                "%s/v2.1/accounts/%s/envelopes"
                                                + "?status=completed"
                                                + "&from_date=%s"
                                                + "&count=%d"
                                                + "&start_position=%d",
                                properties.getBaseUrl(),
                                properties.getAccountId(),
                                fromDate,
                                PAGE_SIZE,
                                startPosition);

                log.info("Polling URI: {}", url);

                return URI.create(url);
        }

        private boolean isCompletedEnvelope(
                        EnvelopeInfo envelope) {

                return "completed".equalsIgnoreCase(
                                envelope.getStatus());
        }
}