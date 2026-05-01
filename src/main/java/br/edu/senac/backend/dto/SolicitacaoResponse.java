package br.edu.senac.backend.dto;

import br.edu.senac.backend.model.enums.StatusSolicitacao;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitacaoResponse {
    private Long id;
    private String descricao;
    private String area;
    private Integer horasSolicitadas;
    private StatusSolicitacao status;
    private LocalDateTime dataCriacao;
    private String nomeAluno;
    private String urlArquivo;
}