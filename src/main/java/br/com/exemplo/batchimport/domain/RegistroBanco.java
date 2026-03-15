package br.com.exemplo.batchimport.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistroBanco {

    private Long id;
    private String codigo;
    private String nome;
    private String email;
    private Integer idade;
    private LocalDateTime dataInclusao;
}
