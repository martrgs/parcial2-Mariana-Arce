package org.example.dto;

import org.example.entity.Consulta;

import java.time.LocalTime;

public class ConsultaResponse {
    private Long id;
    private String nombrePaciente;
    private String motivo;
    private Integer numeroConsultorio;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Long medicoId;
    private String nombreMedico;
    private Long pacienteId;

    public static ConsultaResponse fromEntity(Consulta consulta) {
        ConsultaResponse response = new ConsultaResponse();
        response.setId(consulta.getId());
        response.setNombrePaciente(consulta.getPaciente().getNombre());
        response.setMotivo(consulta.getMotivo());
        response.setNumeroConsultorio(consulta.getConsultorio().getNumero());
        response.setHoraInicio(consulta.getHoraInicio());
        response.setHoraFin(consulta.getHoraFin());
        response.setMedicoId(consulta.getMedico().getId());
        response.setNombreMedico(consulta.getMedico().getNombre());
        response.setPacienteId(consulta.getPaciente().getId());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Integer getNumeroConsultorio() { return numeroConsultorio; }
    public void setNumeroConsultorio(Integer numeroConsultorio) { this.numeroConsultorio = numeroConsultorio; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
}
