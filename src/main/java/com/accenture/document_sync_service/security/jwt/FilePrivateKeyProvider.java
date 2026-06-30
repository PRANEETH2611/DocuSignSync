package com.accenture.document_sync_service.security.jwt;

import com.accenture.document_sync_service.exception.DocumentSyncException;
import com.accenture.document_sync_service.util.PrivateKeyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilePrivateKeyProvider implements PrivateKeyProvider {

    private final PrivateKeyLoader privateKeyLoader;

    @Override
    public PrivateKey getPrivateKey() {

        log.info("Loading RSA private key.");

        String pem = privateKeyLoader.loadPrivateKey();

        try (PEMParser parser = new PEMParser(new StringReader(pem))) {

            Object object = parser.readObject();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            if (object instanceof PEMKeyPair keyPair) {

                log.debug("PKCS#1 private key detected.");

                return converter
                        .getKeyPair(keyPair)
                        .getPrivate();
            }

            if (object instanceof PrivateKeyInfo keyInfo) {

                log.debug("PKCS#8 private key detected.");

                return converter.getPrivateKey(keyInfo);
            }

            throw new DocumentSyncException(
                    "Unsupported private key format."
            );

        } catch (IOException exception) {

            log.error("Unable to parse private key.", exception);

            throw new DocumentSyncException(
                    "Unable to parse private key.",
                    exception
            );
        }
    }
}