package br.com.exemplo.batchimport.batch.processor;

import br.com.exemplo.batchimport.domain.RegistroArquivo;
import br.com.exemplo.batchimport.domain.RegistroProcessado;
import br.com.exemplo.batchimport.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegistroProcessor implements ItemProcessor<RegistroArquivo, RegistroProcessado> {

    private final ValidationService validationService;

    @Override
    public RegistroProcessado process(RegistroArquivo item) {
        validationService.validate(item);

        return RegistroProcessado.builder()
                .codigo(item.getCodigo().trim())
                .nome(item.getNome().trim())
                .email(item.getEmail().trim())
                .idade(Integer.parseInt(item.getIdade().trim()))
                .dataInclusao(LocalDateTime.now())
                .build();
    }
}