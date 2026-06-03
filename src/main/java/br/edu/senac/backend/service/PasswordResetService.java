package br.edu.senac.backend.service;

import br.edu.senac.backend.repository.UsuarioRepository;
import br.edu.senac.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    private final Map<String, Long> ultimaSolicitacao = new ConcurrentHashMap<>();
    private static final long INTERVALO_MINIMO_MS = 60_000;

    public void solicitarReset(String email) {
        log.info("Solicitação de reset de senha para email={}", email);

        long agora = System.currentTimeMillis();
        Long ultima = ultimaSolicitacao.get(email);
        if (ultima != null && agora - ultima < INTERVALO_MINIMO_MS) {
            log.warn("Rate limit atingido para email={}", email);
            throw new RuntimeException("Aguarde 1 minuto antes de solicitar novamente");
        }
        ultimaSolicitacao.put(email, agora);

        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String token = jwtService.gerarTokenReset(email);
            String link = "https://frontend4-xi.vercel.app/redefinir-senha?token=" + token;
            emailService.enviarEmailResetSenha(usuario.getEmail(), usuario.getNome(), link);
            log.info("Link de reset gerado e e-mail enviado para usuario id={}", usuario.getId());
        });
    }

    public void redefinirSenha(String token, String novaSenha, String confirmarSenha) {
        log.info("Tentativa de redefinição de senha");

        if (!novaSenha.equals(confirmarSenha)) {
            throw new RuntimeException("As senhas não coincidem");
        }

        String email;
        try {
            email = jwtService.extrairEmailDoResetToken(token);
        } catch (Exception e) {
            log.warn("Token de reset inválido ou expirado");
            throw new RuntimeException("Token inválido ou expirado");
        }

        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Token inválido ou expirado"));

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        ultimaSolicitacao.remove(email);
        log.info("Senha redefinida com sucesso para usuario id={}", usuario.getId());
    }
}