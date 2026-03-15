CREATE TABLE IF NOT EXISTS registros_enriquecidos (
    id BIGSERIAL PRIMARY KEY,
    registro_id BIGINT NOT NULL,
    codigo VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    idade INTEGER,
    nome_normalizado VARCHAR(255),
    dominio_email VARCHAR(255),
    faixa_etaria VARCHAR(50),
    data_enriquecimento TIMESTAMP NOT NULL
);

ALTER TABLE registros_enriquecidos
ADD CONSTRAINT uk_registros_enriquecidos_registro_id UNIQUE (registro_id);