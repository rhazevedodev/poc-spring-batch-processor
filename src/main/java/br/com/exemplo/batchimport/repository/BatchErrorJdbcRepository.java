package br.com.exemplo.batchimport.repository;

import br.com.exemplo.batchimport.domain.BatchErrorRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchErrorJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(BatchErrorRecord errorRecord) {
        jdbcTemplate.update("""
                INSERT INTO batch_error_records
                (job_execution_id, step_name, raw_line, error_message, created_at)
                VALUES (?, ?, ?, ?, ?)
                """,
                errorRecord.getJobExecutionId(),
                errorRecord.getStepName(),
                errorRecord.getRawLine(),
                errorRecord.getErrorMessage(),
                errorRecord.getCreatedAt()
        );
    }

    public int countByJobExecutionId(Long jobExecutionId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM batch_error_records
                WHERE job_execution_id = ?
                """, Integer.class, jobExecutionId);

        return count == null ? 0 : count;
    }
}