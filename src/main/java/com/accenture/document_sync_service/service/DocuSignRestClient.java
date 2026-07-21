package com.accenture.document_sync_service.service;

import java.io.InputStream;
import java.net.URI;

public interface DocuSignRestClient {

    <T> T get(URI uri, Class<T> responseType);

    InputStream download(URI uri);
}