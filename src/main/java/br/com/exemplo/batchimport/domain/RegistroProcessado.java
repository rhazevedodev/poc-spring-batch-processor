package br.com.exemplo.batchimport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroProcessado {
    private String codigo;
    private String nome;
    private String email;
    private Integer idade;
    private LocalDateTime dataInclusao;
}