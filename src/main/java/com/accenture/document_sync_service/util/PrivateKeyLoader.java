package com.accenture.document_sync_service.util;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrivateKeyLoader {

    private final DocusignProperties docusignProperties;

    /**
     * Loads the configured RSA private key PEM file.
     *
     * @return PEM file content as a String.
     */
    public String loadPrivateKey() {

        Path keyPath = Path.of(docusignProperties.getPrivateKeyPath());

        log.info("Loading RSA private key from configured path.");

        try {

            if (!Files.exists(keyPath)) {
                throw new DocumentSyncException(
                        "Private key file not found: " + keyPath
                );
            }

            String pem = Files.readString(keyPath);

            if (pem==null || pem.isBlank()) {
                throw new DocumentSyncException(
                        "Private key file is empty."
                );
            }

            log.debug("RSA private key loaded successfully.");

            return pem;

        } catch (IOException exception) {

            log.error("Failed to load RSA private key.", exception);

            throw new DocumentSyncException(
                    "Unable to read private key file.",
                    exception
            );
        }
    }
}