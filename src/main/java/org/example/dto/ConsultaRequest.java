package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public class ConsultaRequest {

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 40, message = "El nombre del paciente no debe superar 40 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre solo permite letras, numeros y espacios")
    @Schema(example = "Juan Perez")
    private String nombrePaciente;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 100, message = "El motivo no debe superar 100 caracteres")
    @Schema(example = "Dolor de cabeza")
    private String motivo;

    @NotNull(message = "El numero de consultorio es obligatorio")
    @Positive(message = "El consultorio debe ser positivo")
    @Schema(example = "203")
    private Integer numeroConsultorio;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Schema(type = "string", example = "08:00")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de finalizacion es obligatoria")
    @Schema(type = "string", example = "08:30")
    private LocalTime horaFin;

    @NotNull(message = "El medico es obligatorio")
    @Schema(example = "1")
    private Long medicoId;

    @NotNull(message = "El paciente es obligatorio")
    @Schema(example = "1")
    private Long pacienteId;

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getNumeroConsultorio() {
        return numeroConsultorio;
    }

    public void setNumeroConsultorio(Integer numeroConsultorio) {
        this.numeroConsultorio = numeroConsultorio;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
}
