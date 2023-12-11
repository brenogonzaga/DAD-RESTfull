package com.dra.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dra.backend.models.entities.Usuario;
import com.dra.backend.models.responses.ListarContato;
import com.dra.backend.persistency.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository contatoRepository;

    public List<ListarContato> listarUsuarios() {
        List<Usuario> usuario = contatoRepository.findAll();
        List<ListarContato> listarContatos = usuario.stream().map(user -> ListarContato.from(user)).toList();
        return listarContatos;
    }

    public Optional<ListarContato> listarUsuario(String email) {
        Optional<Usuario> usuario = contatoRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return null;
        }
        ListarContato listarContato = ListarContato.from(usuario.get());
        return Optional.of(listarContato);
    }

    public Optional<Usuario> deletarUsuario(String email) {
        Optional<Usuario> usuario = contatoRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return null;
        }
        contatoRepository.delete(usuario.get());
        return usuario;
    }
}
