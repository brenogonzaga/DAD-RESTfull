package com.dra.backend.models.responses;

import com.dra.backend.models.entities.Usuario;

import lombok.Data;

@Data
public class ListarContato {
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;

    private ListarContato(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();
        this.endereco = usuario.getEndereco();
        this.bairro = usuario.getBairro();
        this.cidade = usuario.getCidade();
        this.estado = usuario.getEstado();
    }

    public static ListarContato from(Usuario usuario) {
        return new ListarContato(usuario);
    }
}
