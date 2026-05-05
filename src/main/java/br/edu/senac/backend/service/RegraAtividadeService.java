package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.RegraAtividadeRequest;
import br.edu.senac.backend.dto.RegraAtividadeResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.RegraAtividade;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.RegraAtividadeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegraAtividadeService {

    private final RegraAtividadeRepository regraAtividadeRepository;
    private final CursoRepository cursoRepository;

    public RegraAtividadeResponse criar(Long cursoId, RegraAtividadeRequest request) {
        log.info("Criando regra de atividade cursoId={}, area={}, limiteHoras={}", cursoId, request.getArea(), request.getLimiteHoras());
        Curso curso = buscarCurso(cursoId);

        boolean jaExiste = regraAtividadeRepository.findByCursoId(cursoId)
                .stream()
                .anyMatch(r -> r.getArea().equalsIgnoreCase(request.getArea()));

        if (jaExiste) {
            log.warn("Regra já existente para cursoId={}, area={}", cursoId, request.getArea());
            throw new RuntimeException("Já existe uma regra para a área '" + request.getArea() + "' neste curso");
        }

        RegraAtividade regra = new RegraAtividade();
        regra.setArea(request.getArea());
        regra.setLimiteHoras(request.getLimiteHoras());
        regra.setLimiteSemestral(request.getLimiteSemestral() != null ? request.getLimiteSemestral() : 0);
        regra.setCurso(curso);

        regraAtividadeRepository.save(regra);
        log.info("Regra de atividade criada com sucesso id={}, cursoId={}, area={}", regra.getId(), cursoId, regra.getArea());
        return toResponse(regra);
    }

    public List<RegraAtividadeResponse> listarPorCurso(Long cursoId) {
        log.debug("Listando regras de atividade para cursoId={}", cursoId);
        buscarCurso(cursoId);
        return regraAtividadeRepository.findByCursoId(cursoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RegraAtividadeResponse atualizar(Long cursoId, Long regraId, RegraAtividadeRequest request) {
        log.info("Atualizando regra id={}, cursoId={}", regraId, cursoId);
        buscarCurso(cursoId);

        RegraAtividade regra = regraAtividadeRepository.findById(regraId)
                .orElseThrow(() -> {
                    log.error("Regra não encontrada id={}", regraId);
                    return new RuntimeException("Regra não encontrada");
                });

        if (!regra.getCurso().getId().equals(cursoId)) {
            log.warn("Regra id={} não pertence ao cursoId={}", regraId, cursoId);
            throw new RuntimeException("Esta regra não pertence ao curso informado");
        }

        boolean areaConflito = regraAtividadeRepository.findByCursoId(cursoId)
                .stream()
                .anyMatch(r -> r.getArea().equalsIgnoreCase(request.getArea()) && !r.getId().equals(regraId));

        if (areaConflito) {
            log.warn("Conflito de área ao atualizar regra id={}, area={}", regraId, request.getArea());
            throw new RuntimeException("Já existe outra regra para a área '" + request.getArea() + "' neste curso");
        }

        regra.setArea(request.getArea());
        regra.setLimiteHoras(request.getLimiteHoras());
        regra.setLimiteSemestral(request.getLimiteSemestral() != null ? request.getLimiteSemestral() : regra.getLimiteSemestral());

        regraAtividadeRepository.save(regra);
        log.info("Regra id={} atualizada com sucesso", regraId);
        return toResponse(regra);
    }

    public void deletar(Long cursoId, Long regraId) {
        log.info("Deletando regra id={}, cursoId={}", regraId, cursoId);
        buscarCurso(cursoId);

        RegraAtividade regra = regraAtividadeRepository.findById(regraId)
                .orElseThrow(() -> {
                    log.error("Regra não encontrada id={}", regraId);
                    return new RuntimeException("Regra não encontrada");
                });

        if (!regra.getCurso().getId().equals(cursoId)) {
            log.warn("Regra id={} não pertence ao cursoId={}", regraId, cursoId);
            throw new RuntimeException("Esta regra não pertence ao curso informado");
        }

        regraAtividadeRepository.deleteById(regraId);
        log.info("Regra id={} deletada com sucesso", regraId);
    }

    private Curso buscarCurso(Long cursoId) {
        return cursoRepository.findById(cursoId)
                .orElseThrow(() -> {
                    log.error("Curso não encontrado id={}", cursoId);
                    return new RuntimeException("Curso não encontrado");
                });
    }

    private RegraAtividadeResponse toResponse(RegraAtividade regra) {
        RegraAtividadeResponse response = new RegraAtividadeResponse();
        response.setId(regra.getId());
        response.setArea(regra.getArea());
        response.setLimiteHoras(regra.getLimiteHoras());
        return response;
    }
}
