package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.UsuarioRequest;
import br.edu.senac.backend.dto.UsuarioResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setPerfil(request.getPerfil());
        usuario.setCursos(new ArrayList<>());

        if (request.getCursoIds() != null && !request.getCursoIds().isEmpty()) {
            List<Curso> cursos = cursoRepository.findAllById(request.getCursoIds());
            usuario.setCursos(cursos);
        }

        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        return toResponse(buscarUsuario(id));
    }

    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarUsuario(id);
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        if (request.getCursoIds() != null) {
            List<Curso> cursos = cursoRepository.findAllById(request.getCursoIds());
            usuario.setCursos(cursos);
        }

        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public void deletar(Long id) {
        buscarUsuario(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setPerfil(usuario.getPerfil());
        return response;
    }

    public UsuarioResponse associarCurso(Long usuarioId, Long cursoId) {
        Usuario usuario = buscarUsuario(usuarioId);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        boolean jaAssociado = usuario.getCursos().stream()
                .anyMatch(c -> c.getId().equals(cursoId));

        if (jaAssociado) {
            throw new RuntimeException("Usuário já está associado a este curso");
        }

        usuario.getCursos().add(curso);
        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public void desassociarCurso(Long usuarioId, Long cursoId) {
        Usuario usuario = buscarUsuario(usuarioId);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        usuario.getCursos().remove(curso);
        usuarioRepository.save(usuario);
    }
}
