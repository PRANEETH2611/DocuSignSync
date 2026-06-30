package com.accenture.document_sync_service.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class RestClientConfigTest {

    @Test
    void shouldCreateRestClient() {

        RestClientConfig config = new RestClientConfig();

        RestClient client =
                config.restClient(RestClient.builder());

        assertNotNull(client);
    }
}