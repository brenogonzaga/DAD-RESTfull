package com.dra.backend.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dra.backend.models.entities.Usuario;
import com.dra.backend.persistency.UsuarioRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    public Optional<Usuario> register(Usuario usuario) {
        Optional<Usuario> user = this.userRepository.findByEmail(usuario.getEmail());
        if (user.isPresent()) {
            return Optional.empty();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(encryptedPassword);
        return Optional.of(this.userRepository.save(usuario));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

}