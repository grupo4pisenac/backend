package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.CursoRequest;
import br.edu.senac.backend.dto.CursoResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoResponse criar(CursoRequest request) {
        Curso curso = new Curso();
        curso.setNome(request.getNome());
        cursoRepository.save(curso);
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
        return response;
    }
}
