package br.com.exemplo.batchimport.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BatchFileControl {
    private Long id;
    private String fileName;
    private String status;
    private Integer totalRead;
    private Integer totalProcessed;
    private Integer totalErrors;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}