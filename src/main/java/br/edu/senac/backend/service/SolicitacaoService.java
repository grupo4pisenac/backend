package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.SolicitacaoRequest;
import br.edu.senac.backend.dto.SolicitacaoResponse;
import br.edu.senac.backend.model.Certificado;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.Solicitacao;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.model.enums.PerfilUsuario;
import br.edu.senac.backend.model.enums.StatusSolicitacao;
import br.edu.senac.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final CertificadoRepository certificadoRepository;
    private final RegraAtividadeRepository regraAtividadeRepository;

    public SolicitacaoResponse criar(Long alunoId, SolicitacaoRequest request) {
        log.info("Criando solicitação alunoId={}, area={}, horasSolicitadas={}", alunoId, request.getArea(), request.getHorasSolicitadas());
        validarAcessoAluno(alunoId);
        Usuario aluno = buscarUsuario(alunoId);
        Curso curso = buscarCurso(request.getCursoId());

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setDescricao(request.getDescricao());
        solicitacao.setArea(request.getArea());
        solicitacao.setHorasSolicitadas(request.getHorasSolicitadas());
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setAluno(aluno);
        solicitacao.setCurso(curso);
        solicitacao.setSemestre(aluno.getSemestreAtual());

        solicitacaoRepository.save(solicitacao);
        log.debug("Solicitação salva id={}, semestre={}", solicitacao.getId(), solicitacao.getSemestre());

        Certificado certificado = new Certificado();
        certificado.setNomeArquivo(request.getUrlCertificado());
        certificado.setUrlArquivo(request.getUrlCertificado());
        certificado.setTipoArquivo("IMAGEM");
        certificado.setSolicitacao(solicitacao);
        certificadoRepository.save(certificado);

        curso.getUsuarios().stream()
                .filter(u -> u.getPerfil() == PerfilUsuario.COORDENADOR)
                .forEach(coordenador -> {
                    log.debug("Notificando coordenador={} sobre nova solicitação", coordenador.getEmail());
                    emailService.enviarEmailNovaSolicitacao(
                            coordenador.getEmail(),
                            aluno.getNome(),
                            solicitacao.getArea()
                    );
                });

        log.info("Solicitação criada com sucesso id={}", solicitacao.getId());
        return toResponse(solicitacao);
    }

    public List<SolicitacaoResponse> listarPorAluno(Long alunoId) {
        log.debug("Listando solicitações do alunoId={}", alunoId);
        validarAcessoAluno(alunoId);
        buscarUsuario(alunoId);
        return solicitacaoRepository.findByAlunoId(alunoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SolicitacaoResponse> listarPorCurso(Long cursoId) {
        log.debug("Listando solicitações do cursoId={}", cursoId);
        buscarCurso(cursoId);
        return solicitacaoRepository.findByCursoId(cursoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SolicitacaoResponse atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        log.info("Atualizando status da solicitação id={} para {}", id, novoStatus);

        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Solicitação não encontrada id={}", id);
                    return new RuntimeException("Solicitação não encontrada");
                });

        if (novoStatus == StatusSolicitacao.APROVADA) {
            regraAtividadeRepository.findByCursoId(solicitacao.getCurso().getId()).stream()
                    .filter(r -> r.getArea().equalsIgnoreCase(solicitacao.getArea()))
                    .findFirst()
                    .ifPresent(regra -> {
                        int horasJaAprovadas = solicitacaoRepository
                                .findByAlunoIdAndCursoIdAndStatus(
                                        solicitacao.getAluno().getId(),
                                        solicitacao.getCurso().getId(),
                                        StatusSolicitacao.APROVADA)
                                .stream()
                                .filter(s -> !s.getId().equals(solicitacao.getId())
                                        && s.getArea().equalsIgnoreCase(solicitacao.getArea())
                                        && solicitacao.getSemestre() != null
                                        && solicitacao.getSemestre().equals(s.getSemestre()))
                                .mapToInt(Solicitacao::getHorasSolicitadas)
                                .sum();

                        int horasRestantes = regra.getLimiteSemestral() - horasJaAprovadas;
                        int horasOriginais = solicitacao.getHorasSolicitadas();
                        int horasAprovadas = Math.max(0, Math.min(horasOriginais, horasRestantes));
                        solicitacao.setHorasSolicitadas(horasAprovadas);

                        log.debug("Cálculo semestral: area={}, horasJaAprovadas={}, horasRestantes={}, horasOriginais={}, horasAprovadas={}",
                                regra.getArea(), horasJaAprovadas, horasRestantes, horasOriginais, horasAprovadas);

                        int totalComAtual = horasJaAprovadas + horasAprovadas;
                        if (totalComAtual > regra.getLimiteSemestral() || horasAprovadas < horasOriginais) {
                            log.warn("Limite semestral atingido: alunoId={}, area={}, semestre={}, total={}h, limite={}h",
                                    solicitacao.getAluno().getId(), regra.getArea(), solicitacao.getSemestre(), totalComAtual, regra.getLimiteSemestral());
                            emailService.enviarEmailLimiteSemestralUltrapassado(
                                    solicitacao.getAluno().getEmail(),
                                    solicitacao.getAluno().getNome(),
                                    solicitacao.getArea(),
                                    solicitacao.getSemestre(),
                                    totalComAtual,
                                    regra.getLimiteSemestral()
                            );
                        }
                    });
        }

        solicitacao.setStatus(novoStatus);
        solicitacaoRepository.save(solicitacao);
        log.info("Status da solicitação id={} atualizado para {} com horasSolicitadas={}", id, novoStatus, solicitacao.getHorasSolicitadas());

        emailService.enviarEmailStatusAtualizado(
                solicitacao.getAluno().getEmail(),
                solicitacao.getAluno().getNome(),
                novoStatus
        );

        return toResponse(solicitacao);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado id={}", id);
                    return new RuntimeException("Usuário não encontrado");
                });
    }

    private Curso buscarCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Curso não encontrado id={}", id);
                    return new RuntimeException("Curso não encontrado");
                });
    }

    private SolicitacaoResponse toResponse(Solicitacao solicitacao) {
        SolicitacaoResponse response = new SolicitacaoResponse();
        response.setId(solicitacao.getId());
        response.setDescricao(solicitacao.getDescricao());
        response.setArea(solicitacao.getArea());
        response.setHorasSolicitadas(solicitacao.getHorasSolicitadas());
        response.setStatus(solicitacao.getStatus());
        response.setDataCriacao(solicitacao.getDataCriacao());
        response.setNomeAluno(solicitacao.getAluno().getNome());
        if (solicitacao.getCertificado() != null) {
            response.setUrlArquivo(solicitacao.getCertificado().getUrlArquivo());
        }
        response.setSemestre(solicitacao.getSemestre());
        return response;
    }

    public List<SolicitacaoResponse> listarPorAlunoEStatus(Long alunoId, StatusSolicitacao status) {
        log.debug("Listando solicitações alunoId={}, status={}", alunoId, status);
        validarAcessoAluno(alunoId);
        buscarUsuario(alunoId);
        return solicitacaoRepository.findByAlunoIdAndStatus(alunoId, status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SolicitacaoResponse> listarPorCursoEStatus(Long cursoId, StatusSolicitacao status) {
        log.debug("Listando solicitações cursoId={}, status={}", cursoId, status);
        buscarCurso(cursoId);
        return solicitacaoRepository.findByCursoIdAndStatus(cursoId, status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SolicitacaoResponse> listarPorAlunoECurso(Long alunoId, Long cursoId) {
        log.debug("Listando solicitações alunoId={}, cursoId={}", alunoId, cursoId);
        validarAcessoAluno(alunoId);
        buscarUsuario(alunoId);
        buscarCurso(cursoId);
        return solicitacaoRepository.findByAlunoIdAndCursoId(alunoId, cursoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Usuário autenticado não encontrado email={}", email);
                    return new RuntimeException("Usuário não encontrado");
                });
    }

    private void validarAcessoAluno(Long alunoId) {
        Usuario autenticado = getUsuarioAutenticado();
        if (autenticado.getPerfil() == PerfilUsuario.ALUNO && !autenticado.getId().equals(alunoId)) {
            log.warn("Acesso negado: alunoAutenticado={} tentou acessar dados de alunoId={}", autenticado.getId(), alunoId);
            throw new RuntimeException("Acesso negado");
        }
    }
}
