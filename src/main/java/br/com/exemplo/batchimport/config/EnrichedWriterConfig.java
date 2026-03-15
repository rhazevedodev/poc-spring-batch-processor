package br.com.exemplo.batchimport.config;

import br.com.exemplo.batchimport.domain.RegistroEnriquecido;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class EnrichedWriterConfig {

    @Bean
    public JdbcBatchItemWriter<RegistroEnriquecido> registroEnriquecidoWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<RegistroEnriquecido>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO registros_enriquecidos
                        (
                            registro_id,
                            codigo,
                            nome,
                            email,
                            idade,
                            nome_normalizado,
                            dominio_email,
                            faixa_etaria,
                            data_enriquecimento
                        )
                        VALUES
                        (
                            :registroId,
                            :codigo,
                            :nome,
                            :email,
                            :idade,
                            :nomeNormalizado,
                            :dominioEmail,
                            :faixaEtaria,
                            :dataEnriquecimento
                        )
                        """)
                .beanMapped()
                .build();
    }
}