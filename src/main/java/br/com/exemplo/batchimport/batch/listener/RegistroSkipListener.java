package br.com.exemplo.batchimport.batch.listener;


import br.com.exemplo.batchimport.domain.BatchErrorRecord;
import br.com.exemplo.batchimport.domain.RegistroArquivo;
import br.com.exemplo.batchimport.repository.BatchErrorJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegistroSkipListener implements SkipListener<RegistroArquivo, Object> {

    private final BatchErrorJdbcRepository batchErrorJdbcRepository;

    @Override
    public void onSkipInProcess(RegistroArquivo item, Throwable t) {
        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();

        String rawLine = String.join(";",
                safe(item.getCodigo()),
                safe(item.getNome()),
                safe(item.getEmail()),
                safe(item.getIdade())
        );

        batchErrorJdbcRepository.save(BatchErrorRecord.builder()
                .jobExecutionId(stepExecution.getJobExecutionId())
                .stepName(stepExecution.getStepName())
                .rawLine(rawLine)
                .errorMessage(t.getMessage())
                .createdAt(LocalDateTime.now())
                .build());
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}