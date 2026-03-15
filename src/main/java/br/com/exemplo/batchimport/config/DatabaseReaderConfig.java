package br.com.exemplo.batchimport.config;

import br.com.exemplo.batchimport.domain.RegistroBanco;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class DatabaseReaderConfig {

    @Bean
    @StepScope
    public JdbcPagingItemReader<RegistroBanco> registroBancoReader(DataSource dataSource) {

        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("id, codigo, nome, email, idade, data_inclusao");
        queryProvider.setFromClause("from registros");

        Map<String, Order> sortKeys = new LinkedHashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return new JdbcPagingItemReaderBuilder<RegistroBanco>()
                .name("registroBancoReader")
                .dataSource(dataSource)
                .pageSize(100)
                .queryProvider(queryProvider)
                .rowMapper((rs, rowNum) -> {
                    RegistroBanco item = new RegistroBanco();
                    item.setId(rs.getLong("id"));
                    item.setCodigo(rs.getString("codigo"));
                    item.setNome(rs.getString("nome"));
                    item.setEmail(rs.getString("email"));
                    item.setIdade(rs.getObject("idade", Integer.class));

                    if (rs.getTimestamp("data_inclusao") != null) {
                        item.setDataInclusao(rs.getTimestamp("data_inclusao").toLocalDateTime());
                    }

                    return item;
                })
                .build();
    }
}