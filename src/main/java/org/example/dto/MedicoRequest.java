package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MedicoRequest {

    @NotBlank
    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Usuario invalido")
    private String username;

    @NotBlank
    @Size(max = 40, message = "El nombre no debe superar 40 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre solo permite letras, numeros y espacios")
    private String nombre;

    @NotBlank
    @Size(min = 4, max = 80)
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
