package com.accenture.document_sync_service.service;

import com.accenture.document_sync_service.dto.AccessToken;

public interface AuthenticationService {

    AccessToken getAccessToken();

}