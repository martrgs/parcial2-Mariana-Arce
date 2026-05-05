package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MedicoUpdateRequest {

    @NotBlank
    @Size(max = 40, message = "El nombre no debe superar 40 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre solo permite letras, numeros y espacios")
    private String nombre;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
