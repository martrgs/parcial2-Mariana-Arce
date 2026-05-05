package org.example.dto;

import org.example.entity.Medico;

public class MedicoResponse {
    private Long id;
    private String nombre;

    public static MedicoResponse fromEntity(Medico medico) {
        MedicoResponse response = new MedicoResponse();
        response.setId(medico.getId());
        response.setNombre(medico.getNombre());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
