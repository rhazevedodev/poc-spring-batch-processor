package br.com.exemplo.batchimport.config;

import br.com.exemplo.batchimport.batch.listener.EnriquecimentoJobExecutionListener;
import br.com.exemplo.batchimport.batch.listener.RegistroEnriquecimentoSkipListener;
import br.com.exemplo.batchimport.batch.processor.RegistroEnriquecimentoProcessor;
import br.com.exemplo.batchimport.domain.RegistroBanco;
import br.com.exemplo.batchimport.domain.RegistroEnriquecido;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class EnrichmentJobConfig {

    @Bean
    public Step enriquecerRegistrosStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcPagingItemReader<RegistroBanco> registroBancoReader,
            RegistroEnriquecimentoProcessor registroEnriquecimentoProcessor,
            JdbcBatchItemWriter<RegistroEnriquecido> registroEnriquecidoWriter,
            RegistroEnriquecimentoSkipListener registroEnriquecimentoSkipListener) {

        return new StepBuilder("enriquecerRegistrosStep", jobRepository)
                .<RegistroBanco, RegistroEnriquecido>chunk(100, transactionManager)
                .reader(registroBancoReader)
                .processor(registroEnriquecimentoProcessor)
                .writer(registroEnriquecidoWriter)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(1000)
                .listener(registroEnriquecimentoSkipListener)
                .build();
    }

    @Bean
    public Job enriquecerRegistrosJob(
            JobRepository jobRepository,
            Step enriquecerRegistrosStep,
            EnriquecimentoJobExecutionListener enriquecimentoJobExecutionListener) {

        return new JobBuilder("enriquecerRegistrosJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(enriquecimentoJobExecutionListener)
                .start(enriquecerRegistrosStep)
                .build();
    }
}