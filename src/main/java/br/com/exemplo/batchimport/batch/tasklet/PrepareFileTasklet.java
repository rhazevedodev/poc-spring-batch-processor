package br.com.exemplo.batchimport.batch.tasklet;

import br.com.exemplo.batchimport.service.FileLocatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class PrepareFileTasklet implements Tasklet {

    private final FileLocatorService fileLocatorService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String filePath = (String) chunkContext.getStepContext()
                .getJobParameters()
                .get("filePath");

        Resource resource = fileLocatorService.getResource(filePath);
        log.info("Arquivo preparado para leitura: {}", resource.getFilename());

        return RepeatStatus.FINISHED;
    }
}