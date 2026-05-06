package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.AreaProgressoDTO;
import br.edu.senac.backend.dto.CursoDropdownResponse;
import br.edu.senac.backend.dto.DashboardAlunoResponse;
import br.edu.senac.backend.dto.DashboardCursoResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final RegraAtividadeRepository regraAtividadeRepository;

    public DashboardAlunoResponse dashboardAluno(Long alunoId, Long cursoId) {
        log.info("Carregando dashboard alunoId={}, cursoId={}", alunoId, cursoId);
        validarAcessoAluno(alunoId);

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        return montarDashboardAluno(aluno, curso);
    }

    public List<CursoDropdownResponse> meusCursos() {
        Usuario autenticado = getUsuarioAutenticado();
        log.debug("Buscando cursos para dropdown usuarioId={}, perfil={}", autenticado.getId(), autenticado.getPerfil());

        List<Curso> cursos = autenticado.getPerfil() == PerfilUsuario.SUPER_ADMIN
                ? cursoRepository.findAll()
                : autenticado.getCursos();

        return cursos.stream().map(c -> {
            CursoDropdownResponse dto = new CursoDropdownResponse();
            dto.setId(c.getId());
            dto.setNome(c.getNome());
            return dto;
        }).toList();
    }

    public DashboardAlunoResponse dashboardMeu(Long cursoId) {
        Usuario aluno = getUsuarioAutenticado();
        log.info("Carregando dashboard próprio alunoId={}, cursoId={}", aluno.getId(), cursoId);

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        return montarDashboardAluno(aluno, curso);
    }

    public DashboardCursoResponse dashboardPorCurso(Long cursoId) {
        Usuario autenticado = getUsuarioAutenticado();
        log.info("Carregando dashboard do curso cursoId={}, usuarioId={}", cursoId, autenticado.getId());

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        if (autenticado.getPerfil() == PerfilUsuario.COORDENADOR) {
            boolean coordenaEsseCurso = autenticado.getCursos().stream()
                    .anyMatch(c -> c.getId().equals(cursoId));
            if (!coordenaEsseCurso) {
                log.warn("Coordenador id={} tentou acessar curso id={} que não coordena", autenticado.getId(), cursoId);
                throw new RuntimeException("Acesso negado");
            }
        }

        List<Usuario> alunos = curso.getUsuarios().stream()
                .filter(u -> u.getPerfil() == PerfilUsuario.ALUNO)
                .toList();

        log.debug("Total de alunos no cursoId={}: {}", cursoId, alunos.size());

        List<DashboardAlunoResponse> dashboardAlunos = alunos.stream()
                .map(aluno -> montarDashboardAluno(aluno, curso))
                .toList();

        DashboardCursoResponse response = new DashboardCursoResponse();
        response.setCursoId(curso.getId());
        response.setNomeCurso(curso.getNome());
        response.setAlunos(dashboardAlunos);
        return response;
    }

    private DashboardAlunoResponse montarDashboardAluno(Usuario aluno, Curso curso) {
        List<Solicitacao> aprovadas = solicitacaoRepository
                .findByAlunoIdAndCursoIdAndStatus(aluno.getId(), curso.getId(), StatusSolicitacao.APROVADA);

        List<RegraAtividade> regras = regraAtividadeRepository.findByCursoId(curso.getId());

        List<AreaProgressoDTO> areas = regras.stream().map(regra -> {
            int horasAprovadas = aprovadas.stream()
                    .filter(s -> s.getArea().equalsIgnoreCase(regra.getArea()))
                    .mapToInt(Solicitacao::getHorasSolicitadas)
                    .sum();

            int horasAprovadasSemestre = aprovadas.stream()
                    .filter(s -> s.getArea().equalsIgnoreCase(regra.getArea())
                            && s.getSemestre() != null
                            && s.getSemestre().equals(aluno.getSemestreAtual()))
                    .mapToInt(Solicitacao::getHorasSolicitadas)
                    .sum();

            AreaProgressoDTO dto = new AreaProgressoDTO();
            dto.setArea(regra.getArea());
            dto.setLimiteHoras(regra.getLimiteHoras());
            dto.setHorasAprovadas(horasAprovadas);
            dto.setConcluido(horasAprovadas >= regra.getLimiteHoras());
            dto.setLimiteSemestral(regra.getLimiteSemestral());
            dto.setHorasAprovadasSemestre(horasAprovadasSemestre);
            dto.setConcluidoSemestre(horasAprovadasSemestre >= regra.getLimiteSemestral());
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
            log.warn("Acesso negado: alunoAutenticado={} tentou acessar dashboard de alunoId={}", autenticado.getId(), alunoId);
            throw new RuntimeException("Acesso negado");
        }
    }
}