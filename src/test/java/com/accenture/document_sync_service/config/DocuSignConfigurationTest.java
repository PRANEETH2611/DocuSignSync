package com.accenture.document_sync_service.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class DocuSignConfigurationTest {

    @Test
    void shouldCreateConfigurationClass() {

        DocuSignConfiguration configuration =
                new DocuSignConfiguration();

        assertNotNull(configuration);
    }
}