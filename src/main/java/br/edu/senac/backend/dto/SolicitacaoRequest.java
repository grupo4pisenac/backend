package br.edu.senac.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitacaoRequest {
    @NotBlank
    private String descricao;

    @NotBlank
    private String area;

    @NotNull
    @Min(1)
    private Integer horasSolicitadas;

    @NotNull
    private Long cursoId;
}
