package com.accenture.document_sync_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.service.DownloadService;
import com.accenture.document_sync_service.service.UploadService;

class DocumentArchiveServiceImplTest {

    @Test
    void shouldArchiveDocumentSuccessfully() {

        DownloadService downloadService = mock(DownloadService.class);
        UploadService uploadService = mock(UploadService.class);

        InputStream inputStream =
                new ByteArrayInputStream("Test".getBytes());

        when(downloadService.downloadCompletedDocument(
                "envelope-123"))
                .thenReturn(inputStream);

        DocumentArchiveServiceImpl service =
                new DocumentArchiveServiceImpl(
                        downloadService,
                        uploadService);

        service.archiveCompletedDocument(
                "envelope-123");

        verify(downloadService)
                .downloadCompletedDocument(
                        "envelope-123");

        verify(uploadService)
                .upload(
                        eq("envelope-123.pdf"),
                        eq(inputStream),
                        eq("application/pdf"));
    }

    @Test
    void shouldThrowDocumentSyncExceptionWhenDownloadFails() {

        DownloadService downloadService = mock(DownloadService.class);
        UploadService uploadService = mock(UploadService.class);

        when(downloadService.downloadCompletedDocument(
                "envelope-123"))
                .thenThrow(new RuntimeException("Download failed"));

        DocumentArchiveServiceImpl service =
                new DocumentArchiveServiceImpl(
                        downloadService,
                        uploadService);

        assertThrows(
                DocumentSyncException.class,
                () -> service.archiveCompletedDocument(
                        "envelope-123"));
    }
}