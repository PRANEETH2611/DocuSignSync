package com.accenture.document_sync_service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.accenture.document_sync_service.config.DocusignProperties;

@SpringBootApplication
@EnableConfigurationProperties(DocusignProperties.class)
@ConfigurationPropertiesScan
public class DocumentSyncServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentSyncServiceApplication.class, args);
	}

}
