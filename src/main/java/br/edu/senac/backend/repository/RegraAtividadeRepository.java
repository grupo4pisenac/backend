package br.edu.senac.backend.repository;

import br.edu.senac.backend.model.RegraAtividade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegraAtividadeRepository extends JpaRepository<RegraAtividade, Long> {
    List<RegraAtividade> findByCursoId(Long cursoId);
}
