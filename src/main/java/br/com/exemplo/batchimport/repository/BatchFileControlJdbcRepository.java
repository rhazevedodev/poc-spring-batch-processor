package br.com.exemplo.batchimport.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BatchFileControlJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insertStart(String fileName, String status, LocalDateTime startedAt) {
        jdbcTemplate.update("""
                INSERT INTO batch_file_control
                (file_name, status, total_read, total_processed, total_errors, started_at)
                VALUES (?, ?, 0, 0, 0, ?)
                """,
                fileName, status, Timestamp.valueOf(startedAt)
        );
    }

    public void finalizeLatest(String fileName, String status, int totalRead, int totalProcessed, int totalErrors, LocalDateTime finishedAt) {
        jdbcTemplate.update("""
                UPDATE batch_file_control
                   SET status = ?,
                       total_read = ?,
                       total_processed = ?,
                       total_errors = ?,
                       finished_at = ?
                 WHERE id = (
                    SELECT id FROM batch_file_control
                     WHERE file_name = ?
                     ORDER BY id DESC
                     LIMIT 1
                 )
                """,
                status,
                totalRead,
                totalProcessed,
                totalErrors,
                Timestamp.valueOf(finishedAt),
                fileName
        );
    }
}