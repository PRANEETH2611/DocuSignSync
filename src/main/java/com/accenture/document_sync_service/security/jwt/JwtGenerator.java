package com.accenture.document_sync_service.security.jwt;

import com.accenture.document_sync_service.security.JwtConstants;
import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.config.DocusignProperties;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtGenerator {

    private final PrivateKeyProvider privateKeyProvider;
    private final DocusignProperties properties;
    private final Clock clock;

    public String generateJwt() {

        log.info("Generating DocuSign JWT assertion.");

        try {

            Instant now = Instant.now(clock);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(properties.getIntegrationKey())
                    .subject(properties.getUserId())
                    .audience(URI.create(properties.getOauthBaseUrl()).getHost())
                    .issueTime(Date.from(now))
                    .expirationTime(
                            Date.from(
                                    now.plusSeconds(
                                            JwtConstants.EXPIRATION_SECONDS
                                    )
                                    )
                    )
                    .claim("scope", JwtConstants.SCOPE)
                    .build();

            JWSHeader header =
                    new JWSHeader(JWSAlgorithm.RS256);

            SignedJWT signedJWT =
                    new SignedJWT(header, claims);

            PrivateKey privateKey =
                    privateKeyProvider.getPrivateKey();

            signedJWT.sign(
                    new RSASSASigner((RSAPrivateKey) privateKey)
            );

            log.debug("JWT generated successfully.");

            return signedJWT.serialize();

        }catch(DocumentSyncException ex)
        {
                throw ex;
        } 
        catch (Exception exception) {

            log.error("Failed to generate JWT.", exception);

            throw new RuntimeException(
                    "Unable to generate JWT.",
                    exception
            );
        }
    }
}