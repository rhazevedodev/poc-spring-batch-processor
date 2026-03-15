package br.com.exemplo.batchimport.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocalFileLocatorService implements FileLocatorService {

    private final DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public Resource getResource(String filePath) {
        Resource resource = resourceLoader.getResource(filePath);

        if (!resource.exists()) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + filePath);
        }

        log.info("Arquivo localizado com sucesso: {}", filePath);
        return resource;
    }
}