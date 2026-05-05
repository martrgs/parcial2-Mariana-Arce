package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank
    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Usuario invalido")
    private String username;

    @NotBlank
    @Size(max = 40, message = "El nombre no debe superar 40 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre solo permite letras, numeros y espacios")
    private String nombreCompleto;

    @NotBlank
    @Size(min = 4, max = 80)
    private String password;

    @NotBlank
    @Pattern(regexp = "ADMINISTRADOR|MEDICO|PACIENTE", message = "Rol no valido")
    private String role;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
