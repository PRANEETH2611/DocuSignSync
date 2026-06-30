package com.accenture.document_sync_service.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class DocuSignSdkClientTest {

    @Test
    void authenticateShouldThrowUntilImplemented() {

        DocuSignSdkClient client = new DocuSignSdkClient();
        
        UnsupportedOperationException exception =
        assertThrows(
                UnsupportedOperationException.class,
                client::authenticate
        );

        assertEquals(
                "Authentication implementation will be added next.",
                exception.getMessage()
        );
    }
}