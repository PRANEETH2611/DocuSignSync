package com.accenture.document_sync_service.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocusignPropertiesTest {

    @Test
    void shouldStoreAndReturnConfigurationValues() {

        DocusignProperties properties = new DocusignProperties();

        properties.setBaseUrl("https://account-d.docusign.com");
        properties.setAccountId("account-id");
        properties.setIntegrationKey("integration-key");
        properties.setUserId("user-id");
        properties.setPrivateKeyPath("/keys/private.key");

        assertEquals("https://account-d.docusign.com", properties.getBaseUrl());
        assertEquals("account-id", properties.getAccountId());
        assertEquals("integration-key", properties.getIntegrationKey());
        assertEquals("user-id", properties.getUserId());
        assertEquals("/keys/private.key", properties.getPrivateKeyPath());
    }
}