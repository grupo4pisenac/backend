package br.edu.senac.backend.service;

import br.edu.senac.backend.dto.LoginRequest;
import br.edu.senac.backend.dto.LoginResponse;
import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.repository.UsuarioRepository;
import br.edu.senac.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.gerarToken(usuario);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setPerfil(usuario.getPerfil().name());
        response.setNome(usuario.getNome());
        return response;
    }
}