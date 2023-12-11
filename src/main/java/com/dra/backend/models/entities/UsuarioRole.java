package com.dra.backend.models.entities;

public enum UsuarioRole {
    USER("USER"), ADMIN("ADMIN");

    private final String role;

    UsuarioRole(String value) {
        this.role = value;
    }

    public String getRole() {
        return role;
    }
}
