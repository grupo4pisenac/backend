package br.edu.senac.backend.repository;

import br.edu.senac.backend.model.Usuario;
import br.edu.senac.backend.model.enums.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByPerfil(PerfilUsuario perfil);}
