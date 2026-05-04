package br.edu.senac.backend.dto;

import lombok.Data;

@Data
public class AreaProgressoDTO {
    private Integer limiteHoras;
    private boolean concluido;
    private Integer horasAprovadas;
    private String area;
    private Integer limiteSemestral;
    private Integer horasAprovadasSemestre;
    private Boolean concluidoSemestre;
}
