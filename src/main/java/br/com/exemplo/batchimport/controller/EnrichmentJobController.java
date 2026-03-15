package br.com.exemplo.batchimport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnrichmentJobController {

    private final JobLauncher jobLauncher;
    private final Job enriquecerRegistrosJob;

    @GetMapping("/jobs/enriquecer")
    public String executar() throws Exception {
        JobExecution execution = jobLauncher.run(
                enriquecerRegistrosJob,
                new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters()
        );

        return "Job de enriquecimento iniciado com sucesso. executionId=" + execution.getId();
    }
}