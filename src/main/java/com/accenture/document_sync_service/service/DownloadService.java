package com.accenture.document_sync_service.service;

import java.io.InputStream;
import java.util.List;
import com.accenture.document_sync_service.dto.docusign.EnvelopeDocument;

public interface DownloadService {

    List<EnvelopeDocument> listDocuments(String envelopeId);

    InputStream downloadCompletedDocument(String envelopeId);
    InputStream downloadDocument(String envelopeId,String documentId);

}