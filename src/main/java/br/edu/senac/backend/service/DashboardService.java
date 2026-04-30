package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.AreaProgressoDTO;
import br.edu.senac.backend.dto.DashboardAlunoResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.RegraAtividade;
import br.edu.senac.backend.model.Solicitacao;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.model.enums.PerfilUsuario;
import br.edu.senac.backend.model.enums.StatusSolicitacao;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.RegraAtividadeRepository;
import br.edu.senac.backend.repository.SolicitacaoRepository;
import br.edu.senac.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final RegraAtividadeRepository regraAtividadeRepository;

    public DashboardAlunoResponse dashboardAluno(Long alunoId, Long cursoId) {
        validarAcessoAluno(alunoId);
        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        List<Solicitacao> aprovadas = solicitacaoRepository
                .findByAlunoIdAndCursoIdAndStatus(alunoId, cursoId, StatusSolicitacao.APROVADA);

        List<RegraAtividade> regras = regraAtividadeRepository.findByCursoId(cursoId);

        List<AreaProgressoDTO> areas = regras.stream().map(regra -> {
            int horasAprovadas = aprovadas.stream()
                    .filter(s -> s.getArea().equalsIgnoreCase(regra.getArea()))
                    .mapToInt(Solicitacao::getHorasSolicitadas)
                    .sum();

            AreaProgressoDTO dto = new AreaProgressoDTO();
            dto.setArea(regra.getArea());
            dto.setHorasAprovadas(horasAprovadas);
            dto.setLimiteHoras(regra.getLimiteHoras());
            dto.setConcluido(horasAprovadas >= regra.getLimiteHoras());
            return dto;
        }).toList();

        int totalAprovadas = areas.stream().mapToInt(AreaProgressoDTO::getHorasAprovadas).sum();
        int totalExigidas = regras.stream().mapToInt(RegraAtividade::getLimiteHoras).sum();

        DashboardAlunoResponse response = new DashboardAlunoResponse();
        response.setNomeAluno(aluno.getNome());
        response.setNomeCurso(curso.getNome());
        response.setAreas(areas);
        response.setTotalHorasAprovadas(totalAprovadas);
        response.setTotalHorasExigidas(totalExigidas);
        return response;
    }

    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private void validarAcessoAluno(Long alunoId) {
        Usuario autenticado = getUsuarioAutenticado();
        if (autenticado.getPerfil() == PerfilUsuario.ALUNO && !autenticado.getId().equals(alunoId)) {
            throw new RuntimeException("Acesso negado");
        }
    }
}
