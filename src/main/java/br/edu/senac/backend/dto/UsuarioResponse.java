package br.edu.senac.backend.dto;

import br.edu.senac.backend.model.enums.PerfilUsuario;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private PerfilUsuario perfil;
    private List<CursoResponse> cursos;
}
