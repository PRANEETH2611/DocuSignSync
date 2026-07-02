package com.accenture.document_sync_service.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.service.DocumentArchiveService;
import com.accenture.document_sync_service.service.DownloadService;
import com.accenture.document_sync_service.service.UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentArchiveServiceImpl
        implements DocumentArchiveService {

    private static final String PDF_CONTENT_TYPE = "application/pdf";

    private final DownloadService downloadService;
    private final UploadService uploadService;

    @Override
    public void archiveCompletedDocument(String envelopeId) {

        validateEnvelopeId(envelopeId);

        String objectName = buildObjectName(envelopeId);

        log.info(
                "Starting archive process for envelope '{}'.",
                envelopeId
        );

        try (InputStream inputStream =
                     downloadService.downloadCompletedDocument(
                             envelopeId)) {

            uploadService.upload(
                    objectName,
                    inputStream,
                    PDF_CONTENT_TYPE
            );

            log.info(
                    "Successfully archived envelope '{}' as '{}'.",
                    envelopeId,
                    objectName
            );

        } catch (IOException exception) {

            log.error(
                    "Failed while closing document stream for envelope '{}'.",
                    envelopeId,
                    exception
            );

            throw new DocumentSyncException(
                    "Unable to archive completed document.",
                    exception
            );

        } catch (Exception exception) {

            log.error(
                    "Failed to archive envelope '{}'.",
                    envelopeId,
                    exception
            );

            throw new DocumentSyncException(
                    "Unable to archive completed document.",
                    exception
            );
        }
    }

    private void validateEnvelopeId(String envelopeId) {

        if (envelopeId == null || envelopeId.isBlank()) {
            throw new IllegalArgumentException(
                    "Envelope ID cannot be null or blank."
            );
        }
    }

    private String buildObjectName(String envelopeId) {

        return envelopeId + ".pdf";
    }
}