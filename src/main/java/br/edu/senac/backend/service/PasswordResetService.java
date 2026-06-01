package br.edu.senac.backend.service;

import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final int EXPIRACAO_MINUTOS = 30;

    public void solicitarReset(String email) {
        log.info("Solicitação de reset de senha para email={}", email);

        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            usuario.setResetToken(token);
            usuario.setResetTokenExpiracao(LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS));
            usuarioRepository.save(usuario);

            emailService.enviarEmailResetSenha(usuario.getEmail(), usuario.getNome(), token);
            log.info("Token de reset gerado e e-mail enviado para usuario id={}", usuario.getId());
        });
    }

    public void redefinirSenha(String token, String novaSenha) {
        log.info("Tentativa de redefinição de senha com token={}", token);

        Usuario usuario = usuarioRepository.findByResetToken(token)
                .orElseThrow(() -> {
                    log.warn("Token de reset inválido ou não encontrado: token={}", token);
                    return new RuntimeException("Token inválido ou expirado");
                });

        if (usuario.getResetTokenExpiracao() == null ||
                usuario.getResetTokenExpiracao().isBefore(LocalDateTime.now())) {
            log.warn("Token de reset expirado para usuario id={}", usuario.getId());
            throw new RuntimeException("Token inválido ou expirado");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiracao(null);
        usuarioRepository.save(usuario);

        log.info("Senha redefinida com sucesso para usuario id={}", usuario.getId());
    }
}