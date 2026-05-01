package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.CursoRequest;
import br.edu.senac.backend.dto.CursoResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.RegraAtividade;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.model.enums.PerfilUsuario;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.RegraAtividadeRepository;
import br.edu.senac.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegraAtividadeRepository regraAtividadeRepository;

    public CursoResponse criar(CursoRequest request) {
        Curso curso = new Curso();
        curso.setNome(request.getNome());
        cursoRepository.save(curso);

        Usuario coordenador = usuarioRepository.findById(request.getCoordenadorId())
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        if (coordenador.getPerfil() != PerfilUsuario.COORDENADOR) {
            throw new RuntimeException("Usuário informado não é um coordenador");
        }

        coordenador.getCursos().add(curso);
        usuarioRepository.save(coordenador);

        return toResponse(curso);
    }

    public List<CursoResponse> listar() {
        return cursoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CursoResponse buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return toResponse(curso);
    }

    public CursoResponse atualizar(Long id, CursoRequest request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        curso.setNome(request.getNome());
        cursoRepository.save(curso);
        return toResponse(curso);
    }

    public void deletar(Long id) {
        cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        cursoRepository.deleteById(id);
    }

    private CursoResponse toResponse(Curso curso) {
        CursoResponse response = new CursoResponse();
        response.setId(curso.getId());
        response.setNome(curso.getNome());

        curso.getUsuarios().stream()
                .filter(u -> u.getPerfil() == PerfilUsuario.COORDENADOR)
                .findFirst()
                .ifPresent(coordenador -> {
                    response.setCoordenadorId(coordenador.getId());
                    response.setCoordenadorNome(coordenador.getNome());
                });

        List<RegraAtividade> regras = regraAtividadeRepository.findByCursoId(curso.getId());
        int total = regras.stream().mapToInt(RegraAtividade::getLimiteHoras).sum();
        response.setTotalHorasExigidas(total);

        return response;
    }
}
