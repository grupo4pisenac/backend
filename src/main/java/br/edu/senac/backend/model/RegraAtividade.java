package br.edu.senac.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "regras_atividade")
@Data
public class RegraAtividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private Integer limiteHoras;

    @Column(name = "limite_horas_semestral")
    private Integer limiteSemestral;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;


}
