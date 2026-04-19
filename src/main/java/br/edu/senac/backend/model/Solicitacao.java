package br.edu.senac.backend.model;

import br.edu.senac.backend.model.enums.StatusSolicitacao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes")
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private String area;
    private Integer horasSolicitadas;
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToOne(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    private Certificado certificado;
}
