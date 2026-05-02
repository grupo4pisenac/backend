package br.edu.senac.backend.dto;

import br.edu.senac.backend.model.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioRequest {
    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    private String senha;

    @NotNull
    private PerfilUsuario perfil;

    private List<Long> cursoIds;
}
