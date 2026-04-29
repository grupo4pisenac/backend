package br.edu.senac.backend.dto;

import lombok.Data;
import java.util.List;

@Data // Isto vai criar os métodos setNomeAluno, setNomeCurso, etc.
public class DashboardAlunoResponse {
    private String nomeAluno;
    private String nomeCurso;
    private List<AreaProgressoDTO> areas;
    private Integer totalHorasAprovadas;
    private Integer totalHorasExigidas;
}