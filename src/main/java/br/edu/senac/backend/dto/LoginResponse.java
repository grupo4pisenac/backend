package br.edu.senac.backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String perfil;
    private String nome;
}
