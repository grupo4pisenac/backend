package br.edu.senac.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "certificados")
@Data
public class Certificado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;
    private String urlArquivo;
    private String tipoArquivo;

    @OneToOne
    @JoinColumn(name = "solicitacao_id")
    private Solicitacao solicitacao;
}
