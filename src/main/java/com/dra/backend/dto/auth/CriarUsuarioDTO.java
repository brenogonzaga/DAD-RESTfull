package com.dra.backend.dto.auth;

import java.util.Optional;

import lombok.*;

@Data
public class CriarUsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;

    public static Optional<String> validarCampos(CriarUsuarioDTO usuario) {
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            return Optional.of("Nome inválido. Por favor, tente novamente.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return Optional.of("Email inválido. Por favor, tente novamente.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            return Optional.of("Senha inválida. Por favor, tente novamente.");
        }
        if (usuario.getTelefone() == null || usuario.getTelefone().isEmpty()) {
            return Optional.of("Telefone inválido. Por favor, tente novamente.");
        }
        if (usuario.getEndereco() == null || usuario.getEndereco().isEmpty()) {
            return Optional.of("Endereço inválido. Por favor, tente novamente.");
        }
        if (usuario.getBairro() == null || usuario.getBairro().isEmpty()) {
            return Optional.of("Bairro inválido. Por favor, tente novamente.");
        }
        if (usuario.getCidade() == null || usuario.getCidade().isEmpty()) {
            return Optional.of("Cidade inválida. Por favor, tente novamente.");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            return Optional.of("Estado inválido. Por favor, tente novamente.");
        }
        return Optional.empty();
    }

}