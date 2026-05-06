package br.edu.senac.backend.controller;

import br.edu.senac.backend.dto.CursoDropdownResponse;
import br.edu.senac.backend.dto.DashboardAlunoResponse;
import br.edu.senac.backend.dto.DashboardCursoResponse;
import br.edu.senac.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/aluno/{alunoId}/curso/{cursoId}")
    @PreAuthorize("hasAnyRole('ALUNO','COORDENADOR','SUPER_ADMIN')")
    public ResponseEntity<DashboardAlunoResponse> dashboardAluno(
            @PathVariable Long alunoId,
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(dashboardService.dashboardAluno(alunoId, cursoId));
    }

    @GetMapping("/meus-cursos")
    @PreAuthorize("hasAnyRole('ALUNO','COORDENADOR','SUPER_ADMIN')")
    public ResponseEntity<List<CursoDropdownResponse>> meusCursos() {
        return ResponseEntity.ok(dashboardService.meusCursos());
    }

    @GetMapping("/meu/curso/{cursoId}")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<DashboardAlunoResponse> dashboardMeu(
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(dashboardService.dashboardMeu(cursoId));
    }

    @GetMapping("/coordenador/curso/{cursoId}")
    @PreAuthorize("hasRole('COORDENADOR')")
    public ResponseEntity<DashboardCursoResponse> dashboardCoordenador(
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(dashboardService.dashboardPorCurso(cursoId));
    }

    @GetMapping("/admin/curso/{cursoId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<DashboardCursoResponse> dashboardAdmin(
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(dashboardService.dashboardPorCurso(cursoId));
    }
}
