package br.edu.senac.backend.dto;

import lombok.Data;

@Data
public class CursoResponse {
    private Long id;
    private String nome;
    private Long coordenadorId;
    private String coordenadorNome;
    private Integer totalHorasExigidas;
}
