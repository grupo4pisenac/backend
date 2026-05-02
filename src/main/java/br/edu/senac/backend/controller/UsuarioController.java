package br.edu.senac.backend.controller;

import br.edu.senac.backend.dto.UsuarioRequest;
import br.edu.senac.backend.dto.UsuarioResponse;
import br.edu.senac.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COORDENADOR')")
    public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.status(201).body(usuarioService.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COORDENADOR')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COORDENADOR')")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping("/coordenadores")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> listarCoordenadores() {
        return ResponseEntity.ok(usuarioService.listarCoordenadores());
    }

    @GetMapping("/alunos")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COORDENADOR')")
    public ResponseEntity<List<UsuarioResponse>> listarAlunos() {
        return ResponseEntity.ok(usuarioService.listarAlunos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @PutMapping("/alunos/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UsuarioResponse> atualizarAluno(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/cursos/{cursoId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UsuarioResponse> associarCurso(
            @PathVariable Long usuarioId,
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(usuarioService.associarCurso(usuarioId, cursoId));
    }

    @DeleteMapping("/{usuarioId}/cursos/{cursoId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> desassociarCurso(
            @PathVariable Long usuarioId,
            @PathVariable Long cursoId) {
        usuarioService.desassociarCurso(usuarioId, cursoId);
        return ResponseEntity.noContent().build();
    }
}
