CREATE TABLE IF NOT EXISTS registros (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    idade INTEGER,
    data_inclusao TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS batch_error_records (
    id BIGSERIAL PRIMARY KEY,
    job_execution_id BIGINT,
    step_name VARCHAR(100),
    raw_line TEXT,
    error_message VARCHAR(1000),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS batch_file_control (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_read INTEGER DEFAULT 0,
    total_processed INTEGER DEFAULT 0,
    total_errors INTEGER DEFAULT 0,
    started_at TIMESTAMP,
    finished_at TIMESTAMP
);