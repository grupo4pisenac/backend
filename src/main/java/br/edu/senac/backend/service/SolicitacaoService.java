package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.SolicitacaoRequest;
import br.edu.senac.backend.dto.SolicitacaoResponse;
import br.edu.senac.backend.model.Certificado;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.Solicitacao;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.model.enums.PerfilUsuario;
import br.edu.senac.backend.model.enums.StatusSolicitacao;
import br.edu.senac.backend.repository.CertificadoRepository;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.SolicitacaoRepository;
import br.edu.senac.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final CertificadoRepository certificadoRepository;

    public SolicitacaoResponse criar(Long alunoId, SolicitacaoRequest request) {
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

        solicitacaoRepository.save(solicitacao);
        Certificado certificado = new Certificado();
        certificado.setNomeArquivo(request.getUrlCertificado());
        certificado.setUrlArquivo(request.getUrlCertificado());
        certificado.setTipoArquivo("IMAGEM");
        certificado.setSolicitacao(solicitacao);
        certificadoRepository.save(certificado);
        curso.getUsuarios().stream()
                .filter(u -> u.getPerfil() == PerfilUsuario.COORDENADOR)
                .forEach(coordenador -> emailService.enviarEmailNovaSolicitacao(
                        coordenador.getEmail(),
                        aluno.getNome(),
                        solicitacao.getArea()
                ));

        return toResponse(solicitacao);
    }

    public List<SolicitacaoResponse> listarPorAluno(Long alunoId) {
        validarAcessoAluno(alunoId);
        buscarUsuario(alunoId);
        return solicitacaoRepository.findByAlunoId(alunoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SolicitacaoResponse> listarPorCurso(Long cursoId) {
        buscarCurso(cursoId);
        return solicitacaoRepository.findByCursoId(cursoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SolicitacaoResponse atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
        solicitacao.setStatus(novoStatus);
        solicitacaoRepository.save(solicitacao);
        emailService.enviarEmailStatusAtualizado(
                solicitacao.getAluno().getEmail(),
                solicitacao.getAluno().getNome(),
                novoStatus
        );
        return toResponse(solicitacao);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private Curso buscarCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
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
        return response;
    }

    public List<SolicitacaoResponse> listarPorAlunoEStatus(Long alunoId, StatusSolicitacao status) {
        validarAcessoAluno(alunoId);
        buscarUsuario(alunoId);
        return solicitacaoRepository.findByAlunoIdAndStatus(alunoId, status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SolicitacaoResponse> listarPorCursoEStatus(Long cursoId, StatusSolicitacao status) {
        buscarCurso(cursoId);
        return solicitacaoRepository.findByCursoIdAndStatus(cursoId, status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SolicitacaoResponse> listarPorAlunoECurso(Long alunoId, Long cursoId) {
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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private void validarAcessoAluno(Long alunoId) {
        Usuario autenticado = getUsuarioAutenticado();
        if (autenticado.getPerfil() == PerfilUsuario.ALUNO && !autenticado.getId().equals(alunoId)) {
            throw new RuntimeException("Acesso negado");
        }
    }
}