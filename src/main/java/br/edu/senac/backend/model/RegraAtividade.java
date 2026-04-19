package br.edu.senac.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "regras_atividade")
public class RegraAtividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private Integer limiteHoras;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;
}
