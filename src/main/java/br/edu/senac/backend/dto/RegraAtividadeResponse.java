package br.edu.senac.backend.dto;

import lombok.Data;

@Data
public class RegraAtividadeResponse {
    private Long id;
    private String area;
    private Integer limiteHoras;
    private Integer limiteSemestral;
}
