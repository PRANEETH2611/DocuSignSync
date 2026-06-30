package com.accenture.document_sync_service.security.jwt;

import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.util.PrivateKeyLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FilePrivateKeyProviderTest {

    @Test
    void shouldThrowUntilImplemented() {

       PrivateKeyLoader loader = mock(PrivateKeyLoader.class);

        when(loader.loadPrivateKey())
            .thenReturn("INVALID KEY");

        FilePrivateKeyProvider provider =
            new FilePrivateKeyProvider(loader);

        assertThrows(
                DocumentSyncException.class,
                provider::getPrivateKey
        );
    }
}