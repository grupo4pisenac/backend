package br.edu.senac.backend.controller;

import br.edu.senac.backend.dto.RegraAtividadeRequest;
import br.edu.senac.backend.dto.RegraAtividadeResponse;
import br.edu.senac.backend.service.RegraAtividadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos/{cursoId}/regras")
@RequiredArgsConstructor
public class RegraAtividadeController {

    private final RegraAtividadeService regraAtividadeService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RegraAtividadeResponse> criar(
            @PathVariable Long cursoId,
            @RequestBody @Valid RegraAtividadeRequest request) {
        return ResponseEntity.status(201).body(regraAtividadeService.criar(cursoId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COORDENADOR')")
    public ResponseEntity<List<RegraAtividadeResponse>> listar(@PathVariable Long cursoId) {
        return ResponseEntity.ok(regraAtividadeService.listarPorCurso(cursoId));
    }

    @PutMapping("/{regraId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RegraAtividadeResponse> atualizar(
            @PathVariable Long cursoId,
            @PathVariable Long regraId,
            @RequestBody @Valid RegraAtividadeRequest request) {
        return ResponseEntity.ok(regraAtividadeService.atualizar(cursoId, regraId, request));
    }

    @DeleteMapping("/{regraId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deletar(
            @PathVariable Long cursoId,
            @PathVariable Long regraId) {
        regraAtividadeService.deletar(cursoId, regraId);
        return ResponseEntity.noContent().build();
    }
}