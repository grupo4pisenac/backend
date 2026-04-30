package br.edu.senac.backend.controller;

import br.edu.senac.backend.dto.CursoRequest;
import br.edu.senac.backend.dto.CursoResponse;
import br.edu.senac.backend.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<CursoResponse> criar(@RequestBody @Valid CursoRequest request) {
        return ResponseEntity.status(201).body(cursoService.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COORDENADOR', 'ALUNO')")
    public ResponseEntity<List<CursoResponse>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COORDENADOR', 'ALUNO')")
    public ResponseEntity<CursoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<CursoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid CursoRequest request) {
        return ResponseEntity.ok(cursoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}