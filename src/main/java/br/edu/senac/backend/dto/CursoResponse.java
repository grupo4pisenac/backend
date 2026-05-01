package br.edu.senac.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CursoResponse {
    private Long id;
    private String nome;
    private Long coordenadorId;
    private String coordenadorNome;
    private Integer totalHorasExigidas;
    private List<RegraAtividadeResponse> areas;
}
