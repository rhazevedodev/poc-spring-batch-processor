package br.com.exemplo.batchimport.service;

import br.com.exemplo.batchimport.domain.RegistroArquivo;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validate(RegistroArquivo item) {
        if (item == null) {
            throw new IllegalArgumentException("Linha nula");
        }

        if (isBlank(item.getCodigo())) {
            throw new IllegalArgumentException("Código obrigatório");
        }

        if (isBlank(item.getNome())) {
            throw new IllegalArgumentException("Nome obrigatório");
        }

        if (isBlank(item.getEmail())) {
            throw new IllegalArgumentException("Email obrigatório");
        }

        if (!item.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (isBlank(item.getIdade())) {
            throw new IllegalArgumentException("Idade obrigatória");
        }

        try {
            int idade = Integer.parseInt(item.getIdade().trim());
            if (idade < 0) {
                throw new IllegalArgumentException("Idade não pode ser negativa");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Idade inválida");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}