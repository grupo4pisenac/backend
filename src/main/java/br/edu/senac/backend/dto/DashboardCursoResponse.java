package br.edu.senac.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardCursoResponse {
    private Long cursoId;
    private String nomeCurso;
    private List<DashboardAlunoResponse> alunos;
}