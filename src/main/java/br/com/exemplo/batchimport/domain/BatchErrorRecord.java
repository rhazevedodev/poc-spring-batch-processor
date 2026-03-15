package br.com.exemplo.batchimport.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BatchErrorRecord {
    private Long jobExecutionId;
    private String stepName;
    private String rawLine;
    private String errorMessage;
    private LocalDateTime createdAt;
}