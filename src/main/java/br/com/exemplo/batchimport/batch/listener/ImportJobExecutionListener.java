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
public class ImportJobExecutionListener implements JobExecutionListener {

    private final BatchErrorJdbcRepository batchErrorJdbcRepository;
    private final BatchFileControlJdbcRepository batchFileControlJdbcRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String filePath = jobExecution.getJobParameters().getString("filePath");
        batchFileControlJdbcRepository.insertStart(filePath, "PROCESSING", LocalDateTime.now());
        log.info("Iniciando job para arquivo {}", filePath);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String filePath = jobExecution.getJobParameters().getString("filePath");

        int readCount = 0;
        int writeCount = 0;

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            readCount += stepExecution.getReadCount();
            writeCount += stepExecution.getWriteCount();
        }

        int errorCount = batchErrorJdbcRepository.countByJobExecutionId(jobExecution.getId());
        String status = jobExecution.getStatus().isUnsuccessful() ? "FAILED" : (errorCount > 0 ? "COMPLETED_WITH_ERRORS" : "COMPLETED");

        batchFileControlJdbcRepository.finalizeLatest(
                filePath,
                status,
                readCount,
                writeCount,
                errorCount,
                LocalDateTime.now()
        );

        log.info("Job finalizado. filePath={}, status={}, read={}, written={}, errors={}",
                filePath, status, readCount, writeCount, errorCount);
    }
}