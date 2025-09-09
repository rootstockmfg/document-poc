package com.rootstock.document_poc.service.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Data;

@Data
@ConfigurationProperties("storage")
@Service
public class StorageProperties {
    private String location = "upload-dir";
}
