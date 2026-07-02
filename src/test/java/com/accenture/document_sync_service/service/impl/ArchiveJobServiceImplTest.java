package com.accenture.document_sync_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.entity.ArchiveJob;
import com.accenture.document_sync_service.enums.ArchiveStatus;
import com.accenture.document_sync_service.repository.ArchiveJobRepository;

class ArchiveJobServiceImplTest {

    @Test
    void shouldSavePendingJob() {

        ArchiveJobRepository repository =
                mock(ArchiveJobRepository.class);

        ArchiveJobServiceImpl service =
                new ArchiveJobServiceImpl(repository);

        when(repository.save(any(ArchiveJob.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArchiveJob job =
                service.savePendingJob("envelope-123");

        assertEquals("envelope-123", job.getEnvelopeId());
        assertEquals(ArchiveStatus.PENDING, job.getArchiveStatus());

        verify(repository).save(any(ArchiveJob.class));
    }

    @Test
    void shouldFindPendingJobs() {

        ArchiveJobRepository repository =
                mock(ArchiveJobRepository.class);

        ArchiveJobServiceImpl service =
                new ArchiveJobServiceImpl(repository);

        ArchiveJob job = ArchiveJob.builder()
                .envelopeId("envelope-123")
                .archiveStatus(ArchiveStatus.PENDING)
                .build();

        when(repository.findByArchiveStatusOrderByCreatedAtAsc(ArchiveStatus.PENDING))
                .thenReturn(List.of(job));

        List<ArchiveJob> jobs =
                service.findPendingJobs();

        assertEquals(1, jobs.size());

        verify(repository)
                .findByArchiveStatusOrderByCreatedAtAsc(ArchiveStatus.PENDING);
    }

    @Test
    void shouldMarkJobAsProcessing() {

        ArchiveJobRepository repository =
                mock(ArchiveJobRepository.class);

        ArchiveJobServiceImpl service =
                new ArchiveJobServiceImpl(repository);

        ArchiveJob job = ArchiveJob.builder()
                .id("1")
                .archiveStatus(ArchiveStatus.PENDING)
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(job));

         service.updateStatus(
        "1",ArchiveStatus.PROCESSING
       );

        assertEquals(
                ArchiveStatus.PROCESSING,
                job.getArchiveStatus());

        verify(repository).save(job);
    }

    @Test
    void shouldMarkJobAsCompleted() {

        ArchiveJobRepository repository =
                mock(ArchiveJobRepository.class);

        ArchiveJobServiceImpl service =
                new ArchiveJobServiceImpl(repository);

        ArchiveJob job = ArchiveJob.builder()
                .id("1")
                .archiveStatus(ArchiveStatus.PENDING)
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(job));

         service.updateStatus(
        "1",ArchiveStatus.COMPLETED
       );

        assertEquals(
                ArchiveStatus.COMPLETED,
                job.getArchiveStatus());

        verify(repository).save(job);
    }

    @Test
    void shouldMarkJobAsFailed() {

        ArchiveJobRepository repository =
                mock(ArchiveJobRepository.class);

        ArchiveJobServiceImpl service =
                new ArchiveJobServiceImpl(repository);

        ArchiveJob job = ArchiveJob.builder()
                .id("1")
                .archiveStatus(ArchiveStatus.PENDING)
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(job));

       service.updateStatus(
        "1",ArchiveStatus.FAILED
       );

        assertEquals(
                ArchiveStatus.FAILED,
                job.getArchiveStatus());

        verify(repository).save(job);
    }

    @Test
    void shouldThrowWhenJobNotFound() {

        ArchiveJobRepository repository =
                mock(ArchiveJobRepository.class);

        ArchiveJobServiceImpl service =
                new ArchiveJobServiceImpl(repository);

        when(repository.findById("1"))
                .thenReturn(Optional.empty());

        assertThrows(
                DocumentSyncException.class,
                () ->  service.updateStatus(
        "1",ArchiveStatus.COMPLETED
       ));
    }
}