package br.edu.senac.backend.service;

import br.edu.senac.backend.repository.UsuarioRepository;
import br.edu.senac.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public void solicitarReset(String email) {
        log.info("Solicitação de reset de senha para email={}", email);

        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String token = jwtService.gerarTokenReset(usuario.getEmail());
            emailService.enviarEmailResetSenha(usuario.getEmail(), usuario.getNome(), token);
            log.info("Token de reset gerado e e-mail enviado para usuario id={}", usuario.getId());
        });
    }

    public void redefinirSenha(String token, String novaSenha) {
        log.info("Tentativa de redefinição de senha");

        String email = jwtService.extrairEmailDoTokenReset(token);

        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuarioRepository.save(usuario);
            log.info("Senha redefinida com sucesso para usuario id={}", usuario.getId());
        });
    }
}