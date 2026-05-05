package org.example.dto;

import org.example.entity.Paciente;

public class PacienteResponse {
    private Long id;
    private String nombre;

    public static PacienteResponse fromEntity(Paciente paciente) {
        PacienteResponse response = new PacienteResponse();
        response.setId(paciente.getId());
        response.setNombre(paciente.getNombre());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
