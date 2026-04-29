package br.edu.senac.backend.repository;

import br.edu.senac.backend.model.Solicitacao;
import br.edu.senac.backend.model.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByAlunoId(Long alunoId);
    List<Solicitacao> findByCursoId(Long cursoId);

    List<Solicitacao> findByAlunoIdAndCursoIdAndStatus(Long alunoId, Long cursoId, StatusSolicitacao status);
}
