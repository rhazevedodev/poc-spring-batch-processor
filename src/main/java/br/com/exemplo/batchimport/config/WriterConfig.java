package br.com.exemplo.batchimport.config;


import br.com.exemplo.batchimport.domain.RegistroProcessado;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class WriterConfig {

    @Bean
    public JdbcBatchItemWriter<RegistroProcessado> registroWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<RegistroProcessado>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO registros
                        (codigo, nome, email, idade, data_inclusao)
                        VALUES (:codigo, :nome, :email, :idade, :dataInclusao)
                        """)
                .beanMapped()
                .build();
    }
}