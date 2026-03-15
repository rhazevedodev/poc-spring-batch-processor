package br.com.exemplo.batchimport.config;

import br.com.exemplo.batchimport.domain.RegistroArquivo;
import br.com.exemplo.batchimport.service.FileLocatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class ReaderConfig {

    private final FileLocatorService fileLocatorService;

    @Bean
    @StepScope
    public FlatFileItemReader<RegistroArquivo> registroReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        Resource resource = fileLocatorService.getResource(filePath);

        return new FlatFileItemReaderBuilder<RegistroArquivo>()
                .name("registroReader")
                .resource(resource)
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names("codigo", "nome", "email", "idade")
                .targetType(RegistroArquivo.class)
                .build();
    }
}