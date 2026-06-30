package com.accenture.document_sync_service.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DocumentSyncExceptionTest {

    @Test
    void shouldStoreMessage() {

        DocumentSyncException exception =
                new DocumentSyncException("Test Error");

        assertEquals("Test Error", exception.getMessage());
    }
}