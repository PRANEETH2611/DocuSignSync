package com.accenture.document_sync_service.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.dto.docusign.EnvelopeDocument;
import com.accenture.document_sync_service.dto.docusign.EnvelopeDocumentsResponse;
import com.accenture.document_sync_service.exception.DocumentSyncException;

import com.accenture.document_sync_service.service.DocuSignRestClient;
import com.accenture.document_sync_service.service.DownloadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocuSignDownloadService implements DownloadService {
        private final DocuSignRestClient docuSignRestClient;
        private final DocusignProperties properties;

        @Override
        public List<EnvelopeDocument> listDocuments(String envelopeId) {

                log.info("Fetching document list for envelope '{}'.", envelopeId);

                URI uri = buildDocumentsUri(envelopeId);

                EnvelopeDocumentsResponse response = docuSignRestClient.get(
                                uri,
                                EnvelopeDocumentsResponse.class);

                if (response == null || response.getEnvelopeDocuments() == null) {
                        throw new DocumentSyncException(
                                        "No documents found for envelope: " + envelopeId);
                }

                log.info(
                                "Retrieved {} documents for envelope '{}'.",
                                response.getEnvelopeDocuments().size(),
                                envelopeId);

                return response.getEnvelopeDocuments();
        }

        @Override
        public InputStream downloadCompletedDocument(String envelopeId) {

                log.info(
                                "Downloading combined document for envelope '{}'.",
                                envelopeId);

                InputStream inputStream = docuSignRestClient.download(
                                buildCombinedDocumentUri(envelopeId));

                if (inputStream == null) {
                        throw new DocumentSyncException(
                                        "Downloaded document stream is empty.");
                }

                return inputStream;
        }

        @Override
        public InputStream downloadDocument(
                        String envelopeId,
                        String documentId) {

                log.info(
                                "Downloading document '{}' for envelope '{}'.",
                                documentId,
                                envelopeId);

                InputStream inputStream = docuSignRestClient.download(
                                buildDocumentUri(
                                                envelopeId,
                                                documentId));

                if (inputStream == null) {
                        throw new DocumentSyncException(
                                        "Downloaded document stream is empty.");
                }

                return inputStream;
        }

        private URI buildDocumentsUri(String envelopeId) {

                return URI.create(
                                properties.getBaseUrl()
                                                + "/v2.1/accounts/"
                                                + properties.getAccountId()
                                                + "/envelopes/"
                                                + envelopeId
                                                + "/documents");
        }

        private URI buildCombinedDocumentUri(
                        String envelopeId) {

                return URI.create(
                                properties.getBaseUrl()
                                                + "/v2.1/accounts/"
                                                + properties.getAccountId()
                                                + "/envelopes/"
                                                + envelopeId
                                                + "/documents/combined");
        }

        private URI buildDocumentUri(
                        String envelopeId,
                        String documentId) {

                return URI.create(
                                properties.getBaseUrl()
                                                + "/v2.1/accounts/"
                                                + properties.getAccountId()
                                                + "/envelopes/"
                                                + envelopeId
                                                + "/documents/"
                                                + documentId);
        }
}