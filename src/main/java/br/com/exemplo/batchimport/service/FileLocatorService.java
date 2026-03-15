package br.com.exemplo.batchimport.service;

import org.springframework.core.io.Resource;

public interface FileLocatorService {
    Resource getResource(String filePath);
}