package br.com.exemplo.batchimport.config;


import br.com.exemplo.batchimport.batch.listener.ImportJobExecutionListener;
import br.com.exemplo.batchimport.batch.listener.RegistroSkipListener;
import br.com.exemplo.batchimport.batch.processor.RegistroProcessor;
import br.com.exemplo.batchimport.batch.tasklet.FinalizeFileTasklet;
import br.com.exemplo.batchimport.batch.tasklet.PrepareFileTasklet;
import br.com.exemplo.batchimport.domain.RegistroArquivo;
import br.com.exemplo.batchimport.domain.RegistroProcessado;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.step.builder.StepBuilder;

@Configuration
@RequiredArgsConstructor
public class BatchJobConfig {

    @Value("${app.batch.chunk-size:100}")
    private Integer chunkSize;

    @Bean
    public Step prepareFileStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            PrepareFileTasklet prepareFileTasklet) {

        return new StepBuilder("prepareFileStep", jobRepository)
                .tasklet(prepareFileTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step processFileStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<RegistroArquivo> registroReader,
            RegistroProcessor registroProcessor,
            JdbcBatchItemWriter<RegistroProcessado> registroWriter,
            RegistroSkipListener registroSkipListener) {

        return new StepBuilder("processFileStep", jobRepository)
                .<RegistroArquivo, RegistroProcessado>chunk(chunkSize, transactionManager)
                .reader(registroReader)
                .processor(registroProcessor)
                .writer(registroWriter)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(1000)
                .listener(registroSkipListener)
                .build();
    }

    @Bean
    public Step finalizeFileStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FinalizeFileTasklet finalizeFileTasklet) {

        return new StepBuilder("finalizeFileStep", jobRepository)
                .tasklet(finalizeFileTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job importacaoArquivoJob(
            JobRepository jobRepository,
            Step prepareFileStep,
            Step processFileStep,
            Step finalizeFileStep,
            ImportJobExecutionListener importJobExecutionListener) {

        return new JobBuilder("importacaoArquivoJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(importJobExecutionListener)
                .start(prepareFileStep)
                .next(processFileStep)
                .next(finalizeFileStep)
                .build();
    }
}