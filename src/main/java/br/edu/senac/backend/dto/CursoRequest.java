package br.edu.senac.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CursoRequest {
    @NotBlank
    private String nome;
}
