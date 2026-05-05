package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.CursoResumoResponse;
import br.edu.senac.backend.dto.UsuarioRequest;
import br.edu.senac.backend.dto.UsuarioResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.model.enums.PerfilUsuario;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse criar(UsuarioRequest request) {
        log.info("Criando usuário email={}, perfil={}", request.getEmail(), request.getPerfil());

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Tentativa de cadastro com email já existente={}", request.getEmail());
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setPerfil(request.getPerfil());
        usuario.setSemestreAtual(request.getSemestreAtual() != null ? request.getSemestreAtual() : 1);
        usuario.setCursos(new ArrayList<>());

        if (request.getCursoIds() != null && !request.getCursoIds().isEmpty()) {
            List<Curso> cursos = cursoRepository.findAllById(request.getCursoIds());
            usuario.setCursos(cursos);
            log.debug("Usuário associado a {} curso(s)", cursos.size());
        }

        usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso id={}, email={}", usuario.getId(), usuario.getEmail());
        return toResponse(usuario);
    }

    public List<UsuarioResponse> listar() {
        log.debug("Listando todos os usuários");
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<UsuarioResponse> listarAlunos() {
        log.debug("Listando todos os alunos");
        return usuarioRepository.findByPerfil(PerfilUsuario.ALUNO)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        log.debug("Buscando usuário id={}", id);
        return toResponse(buscarUsuario(id));
    }

    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        log.info("Atualizando usuário id={}", id);
        Usuario usuario = buscarUsuario(id);
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
            log.debug("Senha atualizada para usuário id={}", id);
        }

        if (request.getSemestreAtual() != null) {
            usuario.setSemestreAtual(request.getSemestreAtual());
        }

        if (request.getCursoIds() != null) {
            List<Curso> cursos = cursoRepository.findAllById(request.getCursoIds());
            usuario.setCursos(cursos);
        }

        usuarioRepository.save(usuario);
        log.info("Usuário id={} atualizado com sucesso", id);
        return toResponse(usuario);
    }

    public void deletar(Long id) {
        log.info("Deletando usuário id={}", id);
        buscarUsuario(id);
        usuarioRepository.deleteById(id);
        log.info("Usuário id={} deletado com sucesso", id);
    }

    public Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado id={}", id);
                    return new RuntimeException("Usuário não encontrado");
                });
    }

    public List<UsuarioResponse> listarCoordenadores() {
        log.debug("Listando todos os coordenadores");
        return usuarioRepository.findByPerfil(PerfilUsuario.COORDENADOR)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setPerfil(usuario.getPerfil());
        response.setSemestreAtual(usuario.getSemestreAtual());
        response.setCursos(usuario.getCursos().stream()
                .map(c -> {
                    CursoResumoResponse cr = new CursoResumoResponse();
                    cr.setId(c.getId());
                    cr.setNome(c.getNome());
                    return cr;
                })
                .toList());
        return response;
    }

    public UsuarioResponse associarCurso(Long usuarioId, Long cursoId) {
        log.info("Associando usuário id={} ao curso id={}", usuarioId, cursoId);
        Usuario usuario = buscarUsuario(usuarioId);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> {
                    log.error("Curso não encontrado id={}", cursoId);
                    return new RuntimeException("Curso não encontrado");
                });

        boolean jaAssociado = usuario.getCursos().stream()
                .anyMatch(c -> c.getId().equals(cursoId));

        if (jaAssociado) {
            log.warn("Usuário id={} já está associado ao curso id={}", usuarioId, cursoId);
            throw new RuntimeException("Usuário já está associado a este curso");
        }

        usuario.getCursos().add(curso);
        usuarioRepository.save(usuario);
        log.info("Usuário id={} associado ao curso id={} com sucesso", usuarioId, cursoId);
        return toResponse(usuario);
    }

    public void desassociarCurso(Long usuarioId, Long cursoId) {
        log.info("Desassociando usuário id={} do curso id={}", usuarioId, cursoId);
        Usuario usuario = buscarUsuario(usuarioId);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> {
                    log.error("Curso não encontrado id={}", cursoId);
                    return new RuntimeException("Curso não encontrado");
                });

        usuario.getCursos().remove(curso);
        usuarioRepository.save(usuario);
        log.info("Usuário id={} desassociado do curso id={} com sucesso", usuarioId, cursoId);
    }
}
