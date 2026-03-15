package br.com.exemplo.batchimport.batch.tasklet;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
public class FinalizeFileTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String filePath = (String) chunkContext.getStepContext()
                .getJobParameters()
                .get("filePath");

        log.info("Finalização do arquivo {}", filePath);
        log.info("Aqui depois você pode mover para pasta processed/ ou error/, ou deletar temporário.");

        return RepeatStatus.FINISHED;
    }
}