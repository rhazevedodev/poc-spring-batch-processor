package br.com.exemplo.batchimport.batch.listener;

import br.com.exemplo.batchimport.repository.BatchErrorJdbcRepository;
import br.com.exemplo.batchimport.repository.BatchFileControlJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnriquecimentoJobExecutionListener implements JobExecutionListener {

    private static final String CONTROL_NAME = "ENRIQUECIMENTO_REGISTROS";

    private final BatchErrorJdbcRepository batchErrorJdbcRepository;
    private final BatchFileControlJdbcRepository batchFileControlJdbcRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        batchFileControlJdbcRepository.insertStart(
                CONTROL_NAME,
                "PROCESSING",
                LocalDateTime.now()
        );

        log.info("Iniciando job de enriquecimento. jobName={}, executionId={}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        int readCount = 0;
        int writeCount = 0;

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            readCount += stepExecution.getReadCount();
            writeCount += stepExecution.getWriteCount();
        }

        int errorCount = batchErrorJdbcRepository.countByJobExecutionId(jobExecution.getId());

        String status = jobExecution.getStatus().isUnsuccessful()
                ? "FAILED"
                : (errorCount > 0 ? "COMPLETED_WITH_ERRORS" : "COMPLETED");

        batchFileControlJdbcRepository.finalizeLatest(
                CONTROL_NAME,
                status,
                readCount,
                writeCount,
                errorCount,
                LocalDateTime.now()
        );

        log.info("Finalizando job de enriquecimento. jobName={}, executionId={}, status={}, readCount={}, writeCount={}, errorCount={}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId(),
                status,
                readCount,
                writeCount,
                errorCount);
    }
}