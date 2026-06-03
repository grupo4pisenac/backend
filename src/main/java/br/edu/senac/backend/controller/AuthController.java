package br.edu.senac.backend.controller;

import br.edu.senac.backend.dto.EsqueciSenhaRequest;
import br.edu.senac.backend.dto.LoginRequest;
import br.edu.senac.backend.dto.LoginResponse;
import br.edu.senac.backend.dto.RedefinirSenhaRequest;
import br.edu.senac.backend.service.AuthService;
import br.edu.senac.backend.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    @Operation(summary = "Realiza login e retorna token JWT")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/esqueci-senha")
    @Operation(summary = "Solicita redefinição de senha — envia token por e-mail")
    public ResponseEntity<Void> esqueciSenha(@RequestBody @Valid EsqueciSenhaRequest request) {
        passwordResetService.solicitarReset(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    @Operation(summary = "Redefine a senha usando o token recebido por e-mail")
    public ResponseEntity<Void> redefinirSenha(@RequestBody @Valid RedefinirSenhaRequest request) {
        passwordResetService.redefinirSenha(
                request.getToken(),
                request.getNovaSenha(),
                request.getConfirmarSenha()
        );
        return ResponseEntity.ok().build();
    }
}