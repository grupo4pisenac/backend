package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.RegraAtividadeRequest;
import br.edu.senac.backend.dto.RegraAtividadeResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.RegraAtividade;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.RegraAtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegraAtividadeService {

    private final RegraAtividadeRepository regraAtividadeRepository;
    private final CursoRepository cursoRepository;

    public RegraAtividadeResponse criar(Long cursoId, RegraAtividadeRequest request) {
        Curso curso = buscarCurso(cursoId);

        boolean jaExiste = regraAtividadeRepository.findByCursoId(cursoId)
                .stream()
                .anyMatch(r -> r.getArea().equalsIgnoreCase(request.getArea()));

        if (jaExiste) {
            throw new RuntimeException("Já existe uma regra para a área '" + request.getArea() + "' neste curso");
        }

        RegraAtividade regra = new RegraAtividade();
        regra.setArea(request.getArea());
        regra.setLimiteHoras(request.getLimiteHoras());
        regra.setCurso(curso);

        regraAtividadeRepository.save(regra);
        return toResponse(regra);
    }

    public List<RegraAtividadeResponse> listarPorCurso(Long cursoId) {
        buscarCurso(cursoId);
        return regraAtividadeRepository.findByCursoId(cursoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RegraAtividadeResponse atualizar(Long cursoId, Long regraId, RegraAtividadeRequest request) {
        buscarCurso(cursoId);

        RegraAtividade regra = regraAtividadeRepository.findById(regraId)
                .orElseThrow(() -> new RuntimeException("Regra não encontrada"));

        if (!regra.getCurso().getId().equals(cursoId)) {
            throw new RuntimeException("Esta regra não pertence ao curso informado");
        }

        boolean areaConflito = regraAtividadeRepository.findByCursoId(cursoId)
                .stream()
                .anyMatch(r -> r.getArea().equalsIgnoreCase(request.getArea()) && !r.getId().equals(regraId));

        if (areaConflito) {
            throw new RuntimeException("Já existe outra regra para a área '" + request.getArea() + "' neste curso");
        }

        regra.setArea(request.getArea());
        regra.setLimiteHoras(request.getLimiteHoras());

        regraAtividadeRepository.save(regra);
        return toResponse(regra);
    }

    public void deletar(Long cursoId, Long regraId) {
        buscarCurso(cursoId);

        RegraAtividade regra = regraAtividadeRepository.findById(regraId)
                .orElseThrow(() -> new RuntimeException("Regra não encontrada"));

        if (!regra.getCurso().getId().equals(cursoId)) {
            throw new RuntimeException("Esta regra não pertence ao curso informado");
        }

        regraAtividadeRepository.deleteById(regraId);
    }

    private Curso buscarCurso(Long cursoId) {
        return cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }

    private RegraAtividadeResponse toResponse(RegraAtividade regra) {
        RegraAtividadeResponse response = new RegraAtividadeResponse();
        response.setId(regra.getId());
        response.setArea(regra.getArea());
        response.setLimiteHoras(regra.getLimiteHoras());
        return response;
    }
}
