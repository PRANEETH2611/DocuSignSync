package com.accenture.document_sync_service.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.exception.DocumentSyncException;

class PrivateKeyLoaderTest {

    @Test
    void shouldThrowExceptionWhenFileDoesNotExist() {

        DocusignProperties properties = new DocusignProperties();
        properties.setPrivateKeyPath("C:/does-not-exist/private.pem");

        PrivateKeyLoader loader = new PrivateKeyLoader(properties);

        assertThrows(
                DocumentSyncException.class,
                loader::loadPrivateKey
        );
    }
    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {

        DocusignProperties properties = new DocusignProperties();
        properties.setPrivateKeyPath("src/test/resources/keys/empty.pem");

        PrivateKeyLoader loader = new PrivateKeyLoader(properties);

        assertThrows(
                DocumentSyncException.class,
                loader::loadPrivateKey
        );
    }
    @Test
    void shouldLoadFileSuccessfully() {

        DocusignProperties properties = new DocusignProperties();
        properties.setPrivateKeyPath("src/test/resources/keys/sample.pem");

        PrivateKeyLoader loader = new PrivateKeyLoader(properties);

        String pem = loader.loadPrivateKey();

        assertNotNull(pem);
        assertFalse(pem.isBlank());
    }
}