package com.dra.backend.persistency;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.dra.backend.models.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);

    UserDetails findUsuarioByEmail(String email);
}
