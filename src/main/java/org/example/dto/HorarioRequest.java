package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "JSON para que un paciente actualice el horario de una cita propia")
public class HorarioRequest {

    @NotNull(message = "Hora inicio obligatoria")
    @Schema(type = "string", example = "09:00")
    private LocalTime horaInicio;

    @NotNull(message = "Hora fin obligatoria")
    @Schema(type = "string", example = "10:30")
    private LocalTime horaFin;

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
}
