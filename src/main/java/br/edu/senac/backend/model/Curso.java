package br.edu.senac.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "cursos")
@Data
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "curso")
    private List<RegraAtividade> regras;

    @ManyToMany(mappedBy = "cursos")
    private List<Usuario> usuarios;
}
