package org.example.dto;

import org.example.entity.Usuario;

public class UsuarioResponse {
    private Long id;
    private String username;
    private String nombreCompleto;
    private String role;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setRole(usuario.getRol().getNombre());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
