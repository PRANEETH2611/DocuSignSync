package com.accenture.document_sync_service.dto.docusign.recipient;

import java.util.List;

import lombok.Data;

@Data
public class RecipientResponse {

    private List<Signer> signers;
}