package br.edu.senac.backend.controller;

import br.edu.senac.backend.dto.SolicitacaoRequest;
import br.edu.senac.backend.dto.SolicitacaoResponse;
import br.edu.senac.backend.model.enums.StatusSolicitacao;
import br.edu.senac.backend.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PostMapping("/aluno/{alunoId}")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<SolicitacaoResponse> criar(
            @PathVariable Long alunoId,
            @RequestBody @Valid SolicitacaoRequest request) {
        return ResponseEntity.status(201).body(solicitacaoService.criar(alunoId, request));
    }

    @GetMapping("/aluno/{alunoId}")
    @PreAuthorize("hasAnyRole('ALUNO', 'COORDENADOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<SolicitacaoResponse>> listarPorAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(solicitacaoService.listarPorAluno(alunoId));
    }

    @GetMapping("/curso/{cursoId}")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<SolicitacaoResponse>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(solicitacaoService.listarPorCurso(cursoId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'SUPER_ADMIN')")
    public ResponseEntity<SolicitacaoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusSolicitacao status) {
        return ResponseEntity.ok(solicitacaoService.atualizarStatus(id, status));
    }

    @GetMapping("/aluno/{alunoId}/filtro")
    @PreAuthorize("hasAnyRole('ALUNO', 'COORDENADOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<SolicitacaoResponse>> listarPorAlunoEStatus(
            @PathVariable Long alunoId,
            @RequestParam StatusSolicitacao status) {
        return ResponseEntity.ok(solicitacaoService.listarPorAlunoEStatus(alunoId, status));
    }

    @GetMapping("/curso/{cursoId}/filtro")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<SolicitacaoResponse>> listarPorCursoEStatus(
            @PathVariable Long cursoId,
            @RequestParam StatusSolicitacao status) {
        return ResponseEntity.ok(solicitacaoService.listarPorCursoEStatus(cursoId, status));
    }

    @GetMapping("/aluno/{alunoId}/curso/{cursoId}")
    @PreAuthorize("hasAnyRole('ALUNO', 'COORDENADOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<SolicitacaoResponse>> listarPorAlunoECurso(
            @PathVariable Long alunoId,
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(solicitacaoService.listarPorAlunoECurso(alunoId, cursoId));
    }
}