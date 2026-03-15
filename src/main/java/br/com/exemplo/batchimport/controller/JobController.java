package br.com.exemplo.batchimport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobLauncher jobLauncher;
    private final Job importacaoArquivoJob;

    @Value("${app.batch.input-file}")
    private String defaultInputFile;

    @GetMapping("/jobs/importar")
    public String executar(@RequestParam(required = false) String filePath) throws Exception {
        String resolvedFilePath = (filePath == null || filePath.isBlank())
                ? defaultInputFile
                : filePath;

        JobExecution execution = jobLauncher.run(
                importacaoArquivoJob,
                new JobParametersBuilder()
                        .addString("filePath", resolvedFilePath)
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters()
        );

        return "Job iniciado com sucesso. executionId=" + execution.getId();
    }
}