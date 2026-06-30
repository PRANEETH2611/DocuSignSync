package com.accenture.document_sync_service.security.jwt;

import com.accenture.document_sync_service.config.DocusignProperties;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtGeneratorTest {

    @Test
    void shouldGenerateJwtSuccessfully() throws Exception {

        // Arrange
        PrivateKeyProvider privateKeyProvider = mock(PrivateKeyProvider.class);

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        when(privateKeyProvider.getPrivateKey())
                .thenReturn(keyPair.getPrivate());

        DocusignProperties properties = new DocusignProperties();
        properties.setIntegrationKey("integration-key");
        properties.setUserId("user-id");
        properties.setOauthBaseUrl("https://account-d.docusign.com");

        Clock clock = Clock.fixed(
                Instant.parse("2026-06-30T12:00:00Z"),
                ZoneOffset.UTC
        );

        JwtGenerator jwtGenerator =
                new JwtGenerator(privateKeyProvider, properties, clock);

        // Act
        String jwt = jwtGenerator.generateJwt();

        // Assert
        assertNotNull(jwt);
        assertFalse(jwt.isBlank());

        SignedJWT parsedJwt = SignedJWT.parse(jwt);

        assertEquals(
                "integration-key",
                parsedJwt.getJWTClaimsSet().getIssuer()
        );

        assertEquals(
                "user-id",
                parsedJwt.getJWTClaimsSet().getSubject()
        );

        assertEquals(
                "account-d.docusign.com",
                parsedJwt.getJWTClaimsSet().getAudience().getFirst()
        );

        assertEquals(
                "signature impersonation",
                parsedJwt.getJWTClaimsSet().getStringClaim("scope")
        );
    }


    @Test
void shouldThrowExceptionWhenPrivateKeyCannotBeLoaded() {

    PrivateKeyProvider provider = mock(PrivateKeyProvider.class);

    when(provider.getPrivateKey())
            .thenThrow(
                    new DocumentSyncException("Unable to load key.")
            );

    DocusignProperties properties = new DocusignProperties();
    properties.setIntegrationKey("integration-key");
    properties.setUserId("user-id");
    properties.setOauthBaseUrl("https://account-d.docusign.com");

    Clock clock = Clock.systemUTC();

    JwtGenerator generator =
            new JwtGenerator(provider, properties, clock);

    assertThrows(
            DocumentSyncException.class,
            generator::generateJwt
    );
}
}