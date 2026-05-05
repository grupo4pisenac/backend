package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.CursoRequest;
import br.edu.senac.backend.dto.CursoResponse;
import br.edu.senac.backend.dto.RegraAtividadeResponse;
import br.edu.senac.backend.model.Curso;
import br.edu.senac.backend.model.RegraAtividade;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.model.enums.PerfilUsuario;
import br.edu.senac.backend.repository.CursoRepository;
import br.edu.senac.backend.repository.RegraAtividadeRepository;
import br.edu.senac.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegraAtividadeRepository regraAtividadeRepository;
    private final EntityManager entityManager;

    @Transactional
    public CursoResponse criar(CursoRequest request) {
        log.info("Criando curso nome={}, coordenadorId={}", request.getNome(), request.getCoordenadorId());

        Curso curso = new Curso();
        curso.setNome(request.getNome());
        curso.setTotalSemestres(request.getTotalSemestres() != null ? request.getTotalSemestres() : 1);
        cursoRepository.save(curso);

        Usuario coordenador = usuarioRepository.findById(request.getCoordenadorId())
                .orElseThrow(() -> {
                    log.error("Coordenador não encontrado id={}", request.getCoordenadorId());
                    return new RuntimeException("Coordenador não encontrado");
                });

        if (coordenador.getPerfil() != PerfilUsuario.COORDENADOR) {
            log.warn("Usuário id={} não é coordenador, perfil={}", coordenador.getId(), coordenador.getPerfil());
            throw new RuntimeException("Usuário informado não é um coordenador");
        }

        coordenador.getCursos().add(curso);
        usuarioRepository.save(coordenador);

        entityManager.flush();
        entityManager.clear();

        Curso cursoAtualizado = cursoRepository.findById(curso.getId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        log.info("Curso criado com sucesso id={}, nome={}", cursoAtualizado.getId(), cursoAtualizado.getNome());
        return toResponse(cursoAtualizado);
    }

    public List<CursoResponse> listar() {
        log.debug("Listando todos os cursos");
        return cursoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CursoResponse buscarPorId(Long id) {
        log.debug("Buscando curso id={}", id);
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Curso não encontrado id={}", id);
                    return new RuntimeException("Curso não encontrado");
                });
        return toResponse(curso);
    }

    @Transactional
    public CursoResponse atualizar(Long id, CursoRequest request) {
        log.info("Atualizando curso id={}", id);
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Curso não encontrado id={}", id);
                    return new RuntimeException("Curso não encontrado");
                });
        curso.setNome(request.getNome());
        if (request.getTotalSemestres() != null) {
            curso.setTotalSemestres(request.getTotalSemestres());
        }
        cursoRepository.save(curso);

        if (request.getCoordenadorId() != null) {
            log.debug("Atualizando coordenador do curso id={} para coordenadorId={}", id, request.getCoordenadorId());
            Usuario novoCoord = usuarioRepository.findById(request.getCoordenadorId())
                    .orElseThrow(() -> {
                        log.error("Coordenador não encontrado id={}", request.getCoordenadorId());
                        return new RuntimeException("Coordenador não encontrado");
                    });

            if (novoCoord.getPerfil() != PerfilUsuario.COORDENADOR) {
                log.warn("Usuário id={} não é coordenador, perfil={}", novoCoord.getId(), novoCoord.getPerfil());
                throw new RuntimeException("Usuário informado não é um coordenador");
            }

            List<Usuario> coordenadoresAtuais = usuarioRepository.findAll().stream()
                    .filter(u -> u.getPerfil() == PerfilUsuario.COORDENADOR
                            && u.getCursos().stream().anyMatch(c -> c.getId().equals(id)))
                    .toList();

            for (Usuario coordAtual : coordenadoresAtuais) {
                coordAtual.getCursos().removeIf(c -> c.getId().equals(id));
                usuarioRepository.save(coordAtual);
            }

            novoCoord.getCursos().add(curso);
            usuarioRepository.save(novoCoord);

            entityManager.flush();
            entityManager.clear();
        }

        Curso cursoAtualizado = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        log.info("Curso id={} atualizado com sucesso", id);
        return toResponse(cursoAtualizado);
    }

    public void deletar(Long id) {
        log.info("Deletando curso id={}", id);
        cursoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Curso não encontrado id={}", id);
                    return new RuntimeException("Curso não encontrado");
                });
        cursoRepository.deleteById(id);
        log.info("Curso id={} deletado com sucesso", id);
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

        List<RegraAtividadeResponse> areas = regras.stream().map(regra -> {
            RegraAtividadeResponse r = new RegraAtividadeResponse();
            r.setId(regra.getId());
            r.setArea(regra.getArea());
            r.setLimiteHoras(regra.getLimiteHoras());
            return r;
        }).toList();
        response.setAreas(areas);

        return response;
    }
}
