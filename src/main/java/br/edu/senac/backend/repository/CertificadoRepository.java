package br.edu.senac.backend.repository;

import br.edu.senac.backend.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
}
