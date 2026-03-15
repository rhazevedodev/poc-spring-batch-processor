package br.com.exemplo.batchimport.batch.processor;

import br.com.exemplo.batchimport.domain.RegistroBanco;
import br.com.exemplo.batchimport.domain.RegistroEnriquecido;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RegistroEnriquecimentoProcessor implements ItemProcessor<RegistroBanco, RegistroEnriquecido> {

    @Override
    public RegistroEnriquecido process(RegistroBanco item) {
        return RegistroEnriquecido.builder()
                .registroId(item.getId())
                .codigo(item.getCodigo())
                .nome(item.getNome())
                .email(item.getEmail())
                .idade(item.getIdade())
                .nomeNormalizado(normalizarNome(item.getNome()))
                .dominioEmail(extrairDominioEmail(item.getEmail()))
                .faixaEtaria(calcularFaixaEtaria(item.getIdade()))
                .dataEnriquecimento(LocalDateTime.now())
                .build();
    }

    private String normalizarNome(String nome) {
        if (nome == null) {
            return null;
        }
        return nome.trim().toUpperCase();
    }

    private String extrairDominioEmail(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }

    private String calcularFaixaEtaria(Integer idade) {
        if (idade == null) {
            return "NAO_INFORMADA";
        }
        if (idade < 18) {
            return "MENOR_DE_IDADE";
        }
        if (idade <= 30) {
            return "JOVEM_ADULTO";
        }
        if (idade <= 59) {
            return "ADULTO";
        }
        return "IDOSO";
    }
}